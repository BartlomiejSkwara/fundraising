package org.example.fundraising.collectionbox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CollectionBoxServiceTest {
    @InjectMocks
    private CollectionBoxService collectionBoxService;
    @Mock
    private CollectionBoxRepository collectionBoxRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerCollectionBox(){
        collectionBoxService.registerCollectionBox();
        verify(collectionBoxRepository,times(1)).save(new CollectionBoxEntity());
    }

}
