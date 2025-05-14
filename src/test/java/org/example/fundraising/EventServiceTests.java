package org.example.fundraising;

import org.example.fundraising.event.EventEntity;
import org.example.fundraising.event.EventRepository;
import org.example.fundraising.event.EventService;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;

public class EventServiceTests {
    @Mock
    private EventRepository collectionBoxRepository;
    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createEventTest() {
        CreateEventRequest request = new CreateEventRequest("Test Event", "USD");
        EventEntity correctEventEntity = EventEntity.builder()
                .balance(0.0)
                .name(request.eventName())
                .currency(request.currencyCode())
                .build();
        eventService.createEvent(request);
        verify(collectionBoxRepository).save(correctEventEntity);
    }



}
