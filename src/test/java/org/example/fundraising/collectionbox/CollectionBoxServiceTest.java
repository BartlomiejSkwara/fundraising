package org.example.fundraising.collectionbox;

import org.example.fundraising.collectionbox.dto.AddCashToBoxRequest;
import org.example.fundraising.common.ExchangeRateService;
import org.example.fundraising.common.exceptions.CollectionBoxNotFoundException;
import org.example.fundraising.common.exceptions.IllegalCurrencyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CollectionBoxServiceTest {
    @InjectMocks
    private CollectionBoxService collectionBoxService;
    @Mock
    private CollectionBoxRepository collectionBoxRepository;
    @Mock
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void registerCollectionBoxTest(){
        collectionBoxService.registerCollectionBox();
        verify(collectionBoxRepository,times(1)).save(new CollectionBoxEntity());
    }


    @Test
    public void addCashWithWrongIdTest(){
        when(collectionBoxRepository.findByIdAndFetchCurrencies(anyLong())).thenReturn(Optional.empty());
        when(exchangeRateService.isCurrencyNoted(any())).thenReturn(true);

        assertThrows(CollectionBoxNotFoundException.class, () -> collectionBoxService.addCash(2L,"EUR",new BigDecimal("10.0")));
        verify(collectionBoxRepository,times(0)).save(any());
    }

    @Test
    public void addCashWithNewCurrencySuccessTest(){
        CollectionBoxEntity retrievedBox = CollectionBoxEntity.builder()
                .id(1L)
                .currencies(new HashMap<>())
                .build();
        AddCashToBoxRequest addedCash = new AddCashToBoxRequest("PLN","20.0");

        when(exchangeRateService.isCurrencyNoted(any())).thenReturn(true);
        when(collectionBoxRepository.findByIdAndFetchCurrencies(anyLong())).thenReturn(Optional.of(retrievedBox));

        collectionBoxService.addCash(1L,addedCash.currencyCode(),new BigDecimal(addedCash.cashAmount()));


        Map<String, BigDecimal> predictedBalances = new HashMap<>();
        predictedBalances.put(addedCash.currencyCode(),new BigDecimal(addedCash.cashAmount()));
        CollectionBoxEntity predictedAnswer = CollectionBoxEntity.builder()
                .id(1L)
                .currencies(predictedBalances)
                .build();
        verify(collectionBoxRepository,times(1)).save(predictedAnswer);
    }

    @Test
    public void addCashWithNotedCurrencySuccessTest(){
        Map<String, BigDecimal> retrievedBalances = new HashMap<>();
        retrievedBalances.put("PLN",new BigDecimal("20.0"));
        CollectionBoxEntity retrievedBox = CollectionBoxEntity.builder()
                .id(1L)
                .currencies(retrievedBalances)
                .build();
        AddCashToBoxRequest addedCash = new AddCashToBoxRequest("PLN","15.0");

        when(exchangeRateService.isCurrencyNoted(any())).thenReturn(true);
        when(collectionBoxRepository.findByIdAndFetchCurrencies(anyLong())).thenReturn(Optional.of(retrievedBox));

        collectionBoxService.addCash(1L,addedCash.currencyCode(),new BigDecimal(addedCash.cashAmount()));


        Map<String, BigDecimal> predictedBalances = new HashMap<>();
        predictedBalances.put("PLN",new BigDecimal("35.0"));

        CollectionBoxEntity predictedAnswer = CollectionBoxEntity.builder()
                .id(1L)
                .currencies(predictedBalances)
                .build();
        verify(collectionBoxRepository,times(1)).save(predictedAnswer);
    }

    @Test
    public void  addCashWithWrongCurrencyTest(){
        when(exchangeRateService.isCurrencyNoted(any())).thenReturn(false);
        assertThrows(IllegalCurrencyException.class, () -> collectionBoxService.addCash(2L,"ERR",new BigDecimal("10.0")));
        verify(collectionBoxRepository,times(0)).save(any());
    }


    @Test
    public void unregisterBoxWithWrongIdTest(){
        when(collectionBoxRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(CollectionBoxNotFoundException.class, () -> collectionBoxService.unregisterBox(2L));
        verify(collectionBoxRepository,times(0)).delete(any());
    }

    @Test
    public void unregisterBoxWithGoodIdTest(){
        when(collectionBoxRepository.findById(any())).thenReturn(Optional.of(new CollectionBoxEntity(2L, null, null)));
        collectionBoxService.unregisterBox(2L);
        verify(collectionBoxRepository,times(1)).delete(any());
    }

    // prcise number
}
