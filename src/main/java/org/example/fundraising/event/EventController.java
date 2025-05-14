package org.example.fundraising.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fundraising.event.dto.CreateEventRequest;
import org.example.fundraising.event.dto.FinancialReportResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Void> createEvent(@Valid @RequestBody CreateEventRequest request) {
        eventService.createEvent(request);
        return  ResponseEntity.ok().build();
    }
    @GetMapping("/financialReport")
    public FinancialReportResponse financialReport() {
        return null;
    }


}
