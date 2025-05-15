package org.example.fundraising.common;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ExchangeRateService {
    private final String baseCurrency = "EUR";
    private final HashMap<String, Double> rates = new HashMap<>();

    public ExchangeRateService() {
        rates.put(baseCurrency, 1.0);
        rates.put("PLN", 0.24);
        rates.put("USD", 0.90);
    }

    ///  pln = 0.24 eur
    ///  eur = 4.23 pln
//        x = 0.24y
//    x/0.24 = y
    public boolean isCurrencyNoted(String currency) {
        return rates.containsKey(currency);
    }
    public double getRate(String fromCurrency, String toCurrency) {
        return 0;
    }


}
