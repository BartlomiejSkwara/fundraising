package org.example.fundrising.collectionbox;


import jakarta.validation.Valid;
import org.example.fundrising.collectionbox.dto.ListCollectionBoxResponse;
import org.example.fundrising.collectionbox.dto.RegisterCollectionBoxRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collectionBox")
public class CollectionBoxController {

    @PostMapping()
    public void registerBox(
            @Valid @RequestBody
            RegisterCollectionBoxRequest request
    ) {

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
    public void addCashToBox(
            @PathVariable Long id,
            @RequestParam Double cash
    ) {

    }

    @PatchMapping("/{id}/emptyBox")
    public void emptyBox(@PathVariable Long id) {

    }


}
