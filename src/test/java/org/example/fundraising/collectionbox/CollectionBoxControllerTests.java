package org.example.fundraising.collectionbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fundraising.collectionbox.dto.AddCashToBoxRequest;
import org.example.fundraising.common.exceptions.IllegalCurrencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CollectionBoxController.class)
public class CollectionBoxControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CollectionBoxService collectionBoxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBoxTest() throws Exception {
        mockMvc.perform(post("/api/collectionBox"))
                .andExpect(status().isOk());
        verify(collectionBoxService,times(1)).registerCollectionBox();
    }
    @Test
    void addCashCorrectDataTest() throws Exception {
        AddCashToBoxRequest request = new AddCashToBoxRequest("PLN","10.00");
        mockMvc.perform(patch("/api/collectionBox/1/addCash")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        verify(collectionBoxService,times(1)).addCash(1L,request.currencyCode(),new BigDecimal(request.cashAmount()));
    }
    @Test()
    void currencyCodeValidationTest() throws Exception {
        List<String> invalidCodes = Arrays.asList("",null,"pln","PL","123","PLNN");
        AddCashToBoxRequest request;
        for (String str : invalidCodes) {
            request = new AddCashToBoxRequest(str,"10.00");
            mockMvc.perform(patch("/api/collectionBox/1/addCash")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isBadRequest());
        }
        verify(collectionBoxService,times(0)).addCash(any(),any(),any());
    }

    @Test
    void cashAmountValidationTest() throws Exception {
        List<String> invalidCashAmount = Arrays.asList("",null,"10.0","-10.00","10","10.111","533000000000.00","ko.ty","tekst");
        AddCashToBoxRequest request;
        for (String str : invalidCashAmount) {
            request = new AddCashToBoxRequest("PLN",str);
            mockMvc.perform(patch("/api/collectionBox/1/addCash")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isBadRequest());
        }

        verify(collectionBoxService,times(0)).addCash(any(),any(),any());
    }

    @Test
    void addCashWrongIdTest() throws Exception {
        List<String> invalidCashAmount = Arrays.asList("abc","1.3");
        AddCashToBoxRequest request = new AddCashToBoxRequest("PLN","10.00");
        for (String str : invalidCashAmount) {
            String url  =  String.format("/api/collectionBox/%s/addCash",str);
            mockMvc.perform(patch(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isBadRequest());
        }

        verify(collectionBoxService,times(0)).addCash(any(),any(),any());
    }

    @Test
    void addCashWithIllegalCurrencyTest() throws Exception {
        AddCashToBoxRequest request = new AddCashToBoxRequest("JPN","10.00");

        doThrow(IllegalCurrencyException.class).when(collectionBoxService).addCash(any(),any(),any());
        mockMvc.perform(patch("/api/collectionBox/1/addCash")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isBadRequest());
    }


    @Test
    void unregisterBoxWrongIdTest() throws Exception {
        List<String> invalidCashAmount = Arrays.asList("abc","1.3");
        for (String str : invalidCashAmount) {
            String url  =  String.format("/api/collectionBox/%s",str);
            mockMvc.perform(delete(url)
            ).andExpect(status().isBadRequest());
        }
        verify(collectionBoxService,times(0)).unregisterBox(any());
    }




}
