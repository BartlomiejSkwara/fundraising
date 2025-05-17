package org.example.fundraising.collectionbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.fundraising.event.EventEntity;
import org.example.fundraising.event.EventRepository;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    public void setup() {
        eventRepo.deleteAll();
        cbRepo.deleteAll();
    }

    @Test
    @Transactional
    public void testBoxCreation() throws Exception {
        quickCollectionBoxRegistrationRequest().andExpect(status().isOk());
        assert (cbRepo.count() == 1);
        CollectionBoxEntity cb = cbRepo.findById(1L).get();
        assert (cb.getEvent() == null);
        assert (cb.getCurrencies() == null);
    }
    @Test
    public void testEventAssignment() throws Exception {
        quickEventCreationRequest("Event 1","PLN");
        quickCollectionBoxRegistrationRequest();
        quickEventAssignment(1L,1L).andExpect(status().isOk());
        EventEntity ev = eventRepo.findById(1L).get();
        CollectionBoxEntity cb = cbRepo.findById(1L).get();
        assert (cbRepo.findById(1L).get().getEvent().equals(ev));
    }






    ResultActions quickEventAssignment(Long boxId, Long eventId) throws Exception {
        return mockMvc.perform(patch(String.format("/api/%s/event/%s", boxId.toString(), eventId.toString()))
        );
    }
    ResultActions quickCollectionBoxRegistrationRequest() throws Exception {
        return mockMvc.perform(post("/api/collectionBox"));
    }
    ResultActions quickEventCreationRequest(String name, String currencyCode) throws Exception {
        return mockMvc.perform(post("/api/event")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateEventRequest(name, currencyCode))));
    }

}
