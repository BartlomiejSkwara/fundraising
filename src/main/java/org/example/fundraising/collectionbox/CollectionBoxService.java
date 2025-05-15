package org.example.fundraising.collectionbox;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionBoxService {
    private final CollectionBoxRepository collectionBoxRepository;
    public void registerCollectionBox() {
        collectionBoxRepository.save(new CollectionBoxEntity());
    }
}
