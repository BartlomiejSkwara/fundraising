package org.example.fundraising.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class EventIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        eventRepo.deleteAll();
    }

    @Test
    void eventInsertionTest() throws Exception {
        CreateEventRequest request = new CreateEventRequest("Test Event", "EUR");

        mockMvc.perform(post("/api/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        assert (eventRepo.count() == 1);
        EventEntity eventEntity  = eventRepo.findById(1L).get();
        EventEntity predicted = EventEntity.builder()
                .id(1L)
                .name(request.eventName())
                .balance(new BigDecimal("0.00"))
                .currency(request.currencyCode()).build();

        assert (eventEntity.equals(predicted));

    }

}
