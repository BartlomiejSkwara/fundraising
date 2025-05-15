package org.example.fundraising.event;

import org.example.fundraising.common.ExchangeRateService;
import org.example.fundraising.common.exceptions.IllegalCurrencyException;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EventServiceTests {
    @Mock
    private EventRepository collectionBoxRepository;
    @Mock
    private ExchangeRateService exchangeRateService;
    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createEventGoodCurrencyTest() {
        CreateEventRequest request = new CreateEventRequest("Test Event", "USD");
        EventEntity correctEventEntity = EventEntity.builder()
                .balance(new BigDecimal("0.0"))
                .name(request.eventName())
                .currency(request.currencyCode())
                .build();

        when(exchangeRateService.isCurrencyNoted(any())).thenReturn(true);

        eventService.createEvent(request);
        verify(collectionBoxRepository,times(1)).save(correctEventEntity);
    }
    @Test
    public void createEventWrongCurrencyTest() {
        CreateEventRequest request = new CreateEventRequest("Test Event", "LOL");

        when(exchangeRateService.isCurrencyNoted(any())).thenReturn(false);

        assertThrows(IllegalCurrencyException.class, () -> eventService.createEvent(request));
        verify(collectionBoxRepository,times(0)).save(any());
    }



}
