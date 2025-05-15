package org.example.fundraising.common.exceptions;

public class IllegalCurrencyException extends RuntimeException {
    public IllegalCurrencyException(String currency) {
        super(String.format("Currency %s is not supported !",currency));
    }
}
