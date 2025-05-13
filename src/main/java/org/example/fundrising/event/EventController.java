package org.example.fundrising.event;

import org.example.fundrising.event.dto.CreateEventRequest;
import org.example.fundrising.event.dto.FinancialReportResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @PostMapping
    public void createEvent(@RequestBody CreateEventRequest request) {

    }
    @GetMapping("/financialReport")
    public FinancialReportResponse financialReport() {
        return null;
    }


}
