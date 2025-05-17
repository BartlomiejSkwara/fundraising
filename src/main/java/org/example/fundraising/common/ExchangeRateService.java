package org.example.fundraising.common;

import org.example.fundraising.common.exceptions.IllegalCurrencyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@Service
public class ExchangeRateService {
    private final String baseCurrency = "PLN";
    private final HashMap<String, BigDecimal> rates = new HashMap<>();

    public ExchangeRateService() {
        rates.put(baseCurrency, new BigDecimal("1.0"));
        rates.put("EUR", new BigDecimal("4.2695"));
        rates.put("USD", new BigDecimal("3.8243"));
    }

    public boolean isCurrencyNoted(String currency) {
        return rates.containsKey(currency);
    }
    public BigDecimal exchangeCurrency(String fromCurrency, BigDecimal currencyAmount, String toCurrency) {
        var from = rates.get(fromCurrency);
        if(from == null) {
            throw new IllegalCurrencyException(fromCurrency);
        }
        var to = rates.get(toCurrency);
        if(to == null) {
            throw new IllegalCurrencyException(toCurrency);
        }

        BigDecimal rate = from.divide(to,5,RoundingMode.HALF_UP);
        return currencyAmount.multiply(rate);
    }



}
