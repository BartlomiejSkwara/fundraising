package org.example.fundraising.collectionbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.fundraising.collectionbox.dto.AddCashToBoxRequest;
import org.example.fundraising.common.ExchangeRateService;
import org.example.fundraising.event.EventEntity;
import org.example.fundraising.event.EventRepository;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CollectionBoxIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CollectionBoxRepository cbRepo;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ExchangeRateService exchangeRateService;



    @BeforeEach
    public void setup() {
        eventRepo.deleteAll();
        cbRepo.deleteAll();
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testBoxCreation() throws Exception {
        collectionBoxRegistrationRequest().andExpect(status().isCreated());
        assert (cbRepo.count() == 1);
        CollectionBoxEntity cb = cbRepo.findById(1L).get();
        assert (cb.getEvent() == null);
        assert (cb.getCurrencies() == null);
    }
    @Test
    @DirtiesContext
    public void testEventAssignment() throws Exception {
        eventCreationRequest("Event 1","PLN");
        collectionBoxRegistrationRequest();
        eventAssignmentRequest(1L,1L).andExpect(status().isOk());
        EventEntity ev = eventRepo.findById(1L).get();
        CollectionBoxEntity cb = cbRepo.findById(1L).get();
        assert (cbRepo.findById(1L).get().getEvent().equals(ev));
    }


    @Test
    @DirtiesContext
    public void testCashAddition() throws Exception {
        eventCreationRequest("Event 1","PLN");
        collectionBoxRegistrationRequest();
        eventAssignmentRequest(1L,1L);
        assert (cbRepo.count() == 1);
        assert (cbRepo.findByIdAndFetchCurrencies(1L).get().getCurrencies().size() == 0);

        cashAdditionRequest(1L,"00.00","USD").andExpect(status().isBadRequest());
        assert (cbRepo.findByIdAndFetchCurrencies(1L).get().getCurrencies().size() == 0);


        cashAdditionRequest(1L,"10.60","EUR").andExpect(status().isOk());
        var balance = cbRepo.findByIdAndFetchCurrencies(1L).get().getCurrencies();
        assert (balance.size() == 1);
        assert (balance.containsKey("EUR") && balance.get("EUR").compareTo(new BigDecimal("10.60")) == 0);


        cashAdditionRequest(1L,"15.00","PLN").andExpect(status().isOk());
        balance = cbRepo.findByIdAndFetchCurrencies(1L).get().getCurrencies();
        assert (balance.size() == 2);
        assert (balance.containsKey("PLN") && balance.get("PLN").compareTo(new BigDecimal("15.0")) == 0);


        cashAdditionRequest(1L,"30.55","EUR").andExpect(status().isOk());
        balance = cbRepo.findByIdAndFetchCurrencies(1L).get().getCurrencies();
        assert (balance.size() == 2);
        assert (balance.containsKey("EUR") && balance.get("EUR").compareTo(new BigDecimal("41.15")) == 0);


    }

    @Test
    @DirtiesContext
    public void emptyBoxTest() throws Exception {
        eventCreationRequest("Event 1","PLN");
        collectionBoxRegistrationRequest();
        eventAssignmentRequest(1L,1L);
        EventEntity event = eventRepo.findById(1L).get();
        assert(event.getBalance().compareTo(BigDecimal.ZERO) == 0);

        cashAdditionRequest(1L,"10.00","EUR");
        cashAdditionRequest(1L,"5.00","PLN");
        emptyBoxRequest(1L).andExpect(status().isOk());
        BigDecimal expectedBalance = new BigDecimal("10.0").multiply(exchangeRateService.getExchangeRate("EUR","PLN"));
        expectedBalance = expectedBalance.add(new BigDecimal("5.0"));
        event = eventRepo.findById(1L).get();
        assert (isPreciseEnough(expectedBalance,event.getBalance(),new BigDecimal("0.01")));


        cashAdditionRequest(1L,"1000000.00","EUR");
        cashAdditionRequest(1L,"500000.00","PLN");
        emptyBoxRequest(1L).andExpect(status().isOk());
        expectedBalance = expectedBalance.add(new BigDecimal("1000000.0").multiply(exchangeRateService.getExchangeRate("EUR","PLN"))) ;
        expectedBalance = expectedBalance.add(new BigDecimal("500000.0"));
        event = eventRepo.findById(1L).get();
        assert (isPreciseEnough(expectedBalance,event.getBalance(),new BigDecimal("0.01")));

    }

    public boolean isPreciseEnough(BigDecimal correct, BigDecimal value, BigDecimal precision){
        return correct.subtract(value).abs().compareTo(precision) < 1;
    }

    @Test
    @DirtiesContext
    public void testBoxUnregistration() throws Exception {
        eventCreationRequest("Event 1","PLN");
        collectionBoxRegistrationRequest();
        eventAssignmentRequest(1L,1L);
        BigDecimal bd = eventRepo.findById(1L).get().getBalance();
        cashAdditionRequest(1L,"10.0","EUR");
        cashAdditionRequest(1L,"5.0","PLN");
        unregisterBoxRequest(1L);
        assert (cbRepo.count() == 0);
        assert (eventRepo.findById(1L).get().getBalance().equals(bd));

    }



    ResultActions emptyBoxRequest(Long boxId) throws Exception {
        return mockMvc.perform(patch(String.format("/api/collectionBoxes/%s/emptyBox", boxId.toString())));
    }
    ResultActions unregisterBoxRequest(Long boxId) throws Exception {
        return mockMvc.perform(delete(String.format("/api/collectionBoxes/%s", boxId.toString())));

    }
    ResultActions cashAdditionRequest(Long boxId, String cash, String currency)throws Exception{
        return mockMvc.perform(patch(String.format("/api/collectionBoxes/%s/addCash", boxId.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new AddCashToBoxRequest(currency,cash))));
    }
    ResultActions eventAssignmentRequest(Long boxId, Long eventId) throws Exception {
        return mockMvc.perform(patch(String.format("/api/collectionBoxes/%s/event/%s", boxId.toString(), eventId.toString()))
        );
    }
    ResultActions collectionBoxRegistrationRequest() throws Exception {
        return mockMvc.perform(post("/api/collectionBoxes"));
    }
    ResultActions eventCreationRequest(String name, String currencyCode) throws Exception {
        return mockMvc.perform(post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateEventRequest(name, currencyCode))));
    }

}
