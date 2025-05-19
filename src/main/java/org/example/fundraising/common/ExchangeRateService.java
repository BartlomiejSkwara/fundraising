package org.example.fundraising.common;

import org.example.fundraising.common.exceptions.IllegalCurrencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;


@Service
public class ExchangeRateService {
    private final HashMap<String, BigDecimal> rates = new HashMap<>();
    private final String baseCurrency = "PLN";
    private final String request = "https://api.frankfurter.dev/v1/latest?base=PLN&symbols=EUR,USD,PLN";
    private static final Logger log = LoggerFactory.getLogger(ExchangeRateService.class);

    public ExchangeRateService(RestTemplate restTemplate) {
        ResponseEntity<FrankfurterApiResponse> str;
        try {
            str = restTemplate.getForEntity(request, FrankfurterApiResponse.class);
        } catch (RestClientException e) {
            str = null;
            log.error(e.getMessage());
        }

        boolean ok = false;
        if (str!=null && str.getStatusCode() == HttpStatus.OK) {
            if(str.getBody()!=null && str.getBody().rates()!=null && str.getBody().base()!=null) {
                for (var v : str.getBody().rates().entrySet()) {
                       rates.put(v.getKey().toUpperCase(),new BigDecimal(v.getValue()));
                }
                rates.put(str.getBody().base(),new BigDecimal("1.00"));

                log.info("Successfully fetched exchange rates from frankfurter api {}",rates.toString());
                ok = true;
            }


        }

        if(!ok) {
            rates.clear();
            rates.put(baseCurrency, new BigDecimal("1.0"));
            rates.put("EUR", new BigDecimal("0.23474"));
            rates.put("USD", new BigDecimal("0.26277"));
            log.warn("Couldn't fetch exchange rates from frankfurter api. App will use default exchange rates {}",rates.toString());
        }


    }

    public boolean isCurrencyNoted(String currency) {
        return rates.containsKey(currency);
    }
    public BigDecimal getExchangeRate(String fromCurrency,String toCurrency){
        var from = rates.get(fromCurrency);
        if(from == null) {
            throw new IllegalCurrencyException(fromCurrency);
        }
        var to = rates.get(toCurrency);
        if(to == null) {
            throw new IllegalCurrencyException(toCurrency);
        }

        return to.divide(from,11,RoundingMode.HALF_UP);
    }
    public BigDecimal exchangeCurrency(String fromCurrency, BigDecimal currencyAmount, String toCurrency) {
        return currencyAmount.multiply(getExchangeRate(fromCurrency,toCurrency));
    }



}
