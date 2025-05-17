package org.example.fundraising.event;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fundraising.common.exceptions.IllegalCurrencyException;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void successfulEventCreationTest() throws Exception {
        CreateEventRequest request = new CreateEventRequest("Test Event", "EUR");

        mockMvc.perform(post("/api/event")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(eventService, times(1)).createEvent(request);

    }
    @Test()
    void eventNameValidationTest() throws Exception {
        List<String> invalidCodesNames  = Arrays.asList("",null," ".repeat(100),"0".repeat(256));
        CreateEventRequest request;
        for(String str:invalidCodesNames){
            request = new CreateEventRequest(str, "EUR");
            mockMvc.perform(post("/api/event")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        verify(eventService, times(0)).createEvent(any());
    }

    @Test()
    void currencyCodeValidationTest() throws Exception {
        List<String> invalidCodes  = Arrays.asList("",null,"pln","PL","123","PLNN");
        CreateEventRequest request;
        for (String str : invalidCodes) {
            request = new  CreateEventRequest("Test Event", str);
            mockMvc.perform(post("/api/event")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
        verify(eventService, times(0)).createEvent(any());

    }


    @Test()
    void illegalCurrencyExceptionTest() throws Exception {
        CreateEventRequest request = new CreateEventRequest("Test Event", "ERR");

        doThrow(IllegalCurrencyException.class).when(eventService).createEvent(request);
        mockMvc.perform(post("/api/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(eventService, times(1)).createEvent(request);

    }

}
