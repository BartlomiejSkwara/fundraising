package org.example.fundraising.collectionbox;


import lombok.RequiredArgsConstructor;
import org.example.fundraising.collectionbox.dto.ListCollectionBoxResponse;
import org.springframework.web.bind.annotation.*;

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
    public void addCashToBox(
            @PathVariable Long id,
            @RequestParam Double cash
    ) {

    }

    @PatchMapping("/{id}/emptyBox")
    public void emptyBox(@PathVariable Long id) {

    }


}
