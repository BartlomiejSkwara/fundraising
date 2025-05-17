package org.example.fundraising.common.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String id) {
        super(String.format("Fundraising Event with id %s couldn't be found !",id));
    }
}
