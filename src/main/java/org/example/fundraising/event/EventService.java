package org.example.fundraising.event;

import lombok.RequiredArgsConstructor;
import org.example.fundraising.common.ExchangeRateService;
import org.example.fundraising.common.exceptions.IllegalCurrencyException;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EventService {

    private final ExchangeRateService exchangeRateService;
    private final EventRepository eventRepository;
    public void createEvent(CreateEventRequest request) {
        if(!exchangeRateService.isCurrencyNoted(request.currencyCode())){
            throw new IllegalCurrencyException(request.currencyCode());
        }

        EventEntity event =  EventEntity.builder()
                .balance(new BigDecimal("0.0"))
                .name(request.eventName())
                .currency(request.currencyCode())
                .build();
        eventRepository.save(event);
    }
}
