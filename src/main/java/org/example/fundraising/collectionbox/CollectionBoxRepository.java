package org.example.fundraising.collectionbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;
import java.util.Optional;

public interface CollectionBoxRepository extends JpaRepository<CollectionBoxEntity, Long> {

    @Query("SELECT b FROM CollectionBoxEntity b LEFT JOIN FETCH b.currencies WHERE b.id = :id ")
    Optional<CollectionBoxEntity> findByIdAndFetchCurrencies(Long id);
}
