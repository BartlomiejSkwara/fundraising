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
    public void registerNewBox() {
        collectionBoxService.registerCollectionBox();
    }

    @GetMapping("/listAll")
    public ListCollectionBoxResponse listBoxes() {
        return null;
    }

    @DeleteMapping("/{id}")
    public void unregisterBox(@PathVariable Long id) {

    }

    @PatchMapping("/{boxId}/event/{eventId}")
    public void assignBox(
            @PathVariable Long boxId,
            @PathVariable Long eventId
    ) {

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

    }


}
