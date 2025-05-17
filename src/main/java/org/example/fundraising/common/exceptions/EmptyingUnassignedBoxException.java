package org.example.fundraising.common.exceptions;

public class EmptyingUnassignedBoxException extends RuntimeException {
    public EmptyingUnassignedBoxException(String id) {
        super(String.format("Can't empty box '%s' due to lack of event assignment !",id));
    }
}
