package org.example.fundraising.collectionbox;

import org.example.fundraising.event.EventRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Map;

@DataJpaTest
public class CollectionBoxRepositoryTest {

    @Autowired
    private CollectionBoxRepository collectionBoxRepository;
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        collectionBoxRepository.deleteAll();
    }

    @Test
    void findByIdAndFetchCurrenciesWrongIdTest() {
        entityManager.persist(new CollectionBoxEntity());
        assert(collectionBoxRepository.findByIdAndFetchCurrencies(3L).isEmpty());

    }
    @Test
    void findByIdAndFetchCurrenciesGoodIdTest() {
        entityManager.persist(new CollectionBoxEntity());
        entityManager.persist(new CollectionBoxEntity());
        CollectionBoxEntity entity  = entityManager.persist(CollectionBoxEntity.builder().currencies(Map.of("PLN",new BigDecimal("5.00"),"EUR",new BigDecimal("15.00"))).build());
        entityManager.persist(new CollectionBoxEntity());
        CollectionBoxEntity found = collectionBoxRepository.findByIdAndFetchCurrencies(3L).orElseThrow();
        assert(Hibernate.isInitialized(found.getCurrencies()));
        assert(entity.equals(found));


    }

}
