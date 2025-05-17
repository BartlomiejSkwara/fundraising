package org.example.fundraising.collectionbox;


import lombok.RequiredArgsConstructor;
import org.example.fundraising.collectionbox.dto.CollectionBoxProjection;
import org.example.fundraising.common.ExchangeRateService;
import org.example.fundraising.common.exceptions.*;
import org.example.fundraising.event.EventEntity;
import org.example.fundraising.event.EventRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {
    private final CollectionBoxRepository collectionBoxRepository;
    private final ExchangeRateService exchangeRateService;
    private final EventRepository eventRepository;

    public void registerCollectionBox() {
        collectionBoxRepository.save(new CollectionBoxEntity());
    }

    public void addCash(Long id, String currency, BigDecimal cash) {

        if(!exchangeRateService.isCurrencyNoted(currency))
            throw  new IllegalCurrencyException(currency);

        CollectionBoxEntity collectionBox = collectionBoxRepository.findByIdAndFetchCurrencies(id).orElseThrow(()->new CollectionBoxNotFoundException(id.toString()));

        BigDecimal currCash = collectionBox.getCurrencies().get(currency);
        if (currCash == null) {
            collectionBox.getCurrencies().put(currency, cash);
        } else {
            collectionBox.getCurrencies().put(currency,cash.add(currCash)) ;
        }

        collectionBoxRepository.save(collectionBox);

    }

    public void unregisterBox(Long id) {
        CollectionBoxEntity collectionBox = collectionBoxRepository.findById(id).orElseThrow(()->new CollectionBoxNotFoundException(id.toString()));
        collectionBoxRepository.delete(collectionBox);

    }

    public void assignEvent(Long boxId, Long eventId) {
        CollectionBoxEntity collectionBox = collectionBoxRepository.findByIdAndFetchCurrencies(boxId).orElseThrow(()->new CollectionBoxNotFoundException(boxId.toString()));
        Map<String,BigDecimal> currencies = collectionBox.getCurrencies();
        if(currencies != null && !currencies.isEmpty()){
            for(BigDecimal value : currencies.values()){
                if(value.compareTo(BigDecimal.ZERO)!=0){
                    throw new EventAssignmentToBoxWithBalance(boxId.toString());
                }
            }
        }

        EventEntity ev  = eventRepository.findById(eventId).orElseThrow(()->new EventNotFoundException(eventId.toString()));
        collectionBox.setEvent(ev);
        collectionBoxRepository.save(collectionBox);


    }

    public void emptyBox(Long boxId) {
        CollectionBoxEntity collectionBox = collectionBoxRepository.findByIdAndFetchCurrencies(boxId).orElseThrow(()->new CollectionBoxNotFoundException(boxId.toString()));
        EventEntity ev = collectionBox.getEvent();
        if (ev == null) {
            throw new EmptyingUnassignedBoxException(boxId.toString());
        }

        BigDecimal sum = new BigDecimal("0");
        for(var value : collectionBox.getCurrencies().entrySet()){
            sum = sum.add(exchangeRateService.exchangeCurrency(value.getKey(),value.getValue(),ev.getCurrency()));
        }

        ev.setBalance(ev.getBalance().add(sum));
        collectionBox.setCurrencies(new HashMap<>());

        collectionBoxRepository.save(collectionBox);
        eventRepository.save(ev);
    }

    public Set<CollectionBoxProjection> listBoxes() {
        return collectionBoxRepository.listBoxProjections();
    }
}
