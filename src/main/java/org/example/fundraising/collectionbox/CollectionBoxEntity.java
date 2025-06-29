package org.example.fundraising.collectionbox;

import jakarta.persistence.*;
import lombok.*;
import org.example.fundraising.event.EventEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionBoxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private EventEntity event;

    @ElementCollection()
    @CollectionTable(name = "currency_balance")
    @MapKeyColumn(name = "currency", length = 3)
    @Column(nullable = false)
    private Map<String, BigDecimal> currencies;

}
