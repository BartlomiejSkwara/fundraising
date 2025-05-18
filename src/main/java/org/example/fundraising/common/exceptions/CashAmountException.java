package org.example.fundraising.common.exceptions;

public class CashAmountException extends RuntimeException{
    public CashAmountException(String currency) {
        super(String.format("Specified cash amount must be greater then zero and '%s' doesn't meet this criteria !",currency));
    }
}
