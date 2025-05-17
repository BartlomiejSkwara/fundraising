package org.example.fundraising.collectionbox;

import org.example.fundraising.collectionbox.dto.CollectionBoxProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface CollectionBoxRepository extends JpaRepository<CollectionBoxEntity, Long> {

    @Query("SELECT b FROM CollectionBoxEntity b LEFT JOIN FETCH b.currencies WHERE b.id = :id ")
    Optional<CollectionBoxEntity> findByIdAndFetchCurrencies(Long id);

    @Query( "SELECT b.id AS id," +
            "CASE WHEN b.event IS NOT NULL THEN true ELSE false END AS assignedToEvent, " +
            "CASE WHEN b.currencies IS EMPTY THEN true ELSE false END AS empty " +
            "FROM CollectionBoxEntity b")
    Set<CollectionBoxProjection> listBoxProjections();

}
