package org.example.fundraising.collectionbox;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.fundraising.collectionbox.dto.AddCashToBoxRequest;
import org.example.fundraising.collectionbox.dto.CollectionBoxProjection;
import org.example.fundraising.collectionbox.dto.ListCollectionBoxResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/collectionBoxes")
@RequiredArgsConstructor
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;

    @PostMapping()
    public ResponseEntity<CollectionBoxEntity> registerNewBox() {
        CollectionBoxEntity cb = collectionBoxService.registerCollectionBox();
        return ResponseEntity
                .created(URI.create("/api/collectionBoxes/" + cb.getId()))
                .body(cb);
    }

    @GetMapping("/listAll")
    public ResponseEntity<Set<CollectionBoxProjection>> listBoxes() {
        return ResponseEntity.ok(collectionBoxService.listBoxes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unregisterBox(@PathVariable Long id) {
        collectionBoxService.unregisterBox(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{boxId}/event/{eventId}")
    public ResponseEntity<Void> assignEvent(
            @PathVariable Long boxId,
            @PathVariable Long eventId
    ) {
        collectionBoxService.assignEvent(boxId,eventId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/addCash")
    public ResponseEntity<Void> addCashToBox(
            @PathVariable Long id,
            @RequestBody @Valid AddCashToBoxRequest addCashToBoxRequest
    ) {
        collectionBoxService.addCash(id,addCashToBoxRequest.currencyCode(),new BigDecimal(addCashToBoxRequest.cashAmount()));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/emptyBox")
    public void emptyBox(@PathVariable Long id) {
        collectionBoxService.emptyBox(id);
    }


}
