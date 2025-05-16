package org.example.fundraising.collectionbox;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fundraising.collectionbox.dto.AddCashToBoxRequest;
import org.example.fundraising.common.ExchangeRateService;
import org.example.fundraising.common.exceptions.CollectionBoxNotFoundException;
import org.example.fundraising.common.exceptions.IllegalCurrencyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {
    private final CollectionBoxRepository collectionBoxRepository;
    private final ExchangeRateService exchangeRateService;
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
}
