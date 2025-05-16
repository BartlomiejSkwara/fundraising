package org.example.fundraising.common.exceptions;

public class CollectionBoxNotFoundException extends RuntimeException {
    public CollectionBoxNotFoundException(String id) {
        super(String.format("Collection box with id %s couldn't be found !",id));
    }
}
