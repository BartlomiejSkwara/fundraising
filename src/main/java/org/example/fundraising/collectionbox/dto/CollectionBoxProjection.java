package org.example.fundraising.collectionbox.dto;

public interface CollectionBoxProjection {
    Long getId();
    boolean isAssignedToEvent();
    boolean isEmpty();
}
