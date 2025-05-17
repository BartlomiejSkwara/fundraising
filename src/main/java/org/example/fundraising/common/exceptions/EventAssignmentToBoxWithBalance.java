package org.example.fundraising.common.exceptions;

public class EventAssignmentToBoxWithBalance extends RuntimeException{
    public EventAssignmentToBoxWithBalance(String id) {
        super(String.format("Can't assign box '%s' to any event due to existing box balance !",id));
    }
}
