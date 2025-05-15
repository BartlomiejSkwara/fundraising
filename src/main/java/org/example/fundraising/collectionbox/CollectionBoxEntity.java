package org.example.fundraising.collectionbox;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.fundraising.event.EventEntity;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class CollectionBoxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EventEntity event;

}
