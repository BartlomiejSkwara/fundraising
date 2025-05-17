package org.example.fundraising.collectionbox;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.fundraising.collectionbox.dto.AddCashToBoxRequest;
import org.example.fundraising.collectionbox.dto.ListCollectionBoxResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/collectionBox")
@RequiredArgsConstructor
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;

    @PostMapping()
    public ResponseEntity<Void> registerNewBox() {
        collectionBoxService.registerCollectionBox();
        System.out.println("Registered new collection box");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listAll")
    public ListCollectionBoxResponse listBoxes() {
        return null;
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
        System.out.println("cash added");
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/emptyBox")
    public void emptyBox(@PathVariable Long id) {

    }


}
