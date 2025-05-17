package org.example.fundraising.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fundraising.collectionbox.CollectionBoxEntity;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.example.fundraising.event.dto.EventReportProjection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventEntity> createEvent(@Valid @RequestBody CreateEventRequest request) {
        EventEntity ev = eventService.createEvent(request);
        return ResponseEntity
                .created(URI.create("/api/event/" + ev.getId()))
                .body(ev);
    }
    @GetMapping("/financialReport")
    public Set<EventReportProjection> financialReport() {
        return eventService.getFinancialReport();
    }


}
