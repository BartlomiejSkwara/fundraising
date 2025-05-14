package org.example.fundraising.event;

import lombok.RequiredArgsConstructor;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    public void createEvent(CreateEventRequest request) {
        EventEntity event =  EventEntity.builder()
                .balance(0.0)
                .name(request.eventName())
                .currency(request.currencyCode())
                .build();
        eventRepository.save(event);
    }
}
