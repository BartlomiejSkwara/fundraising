package org.example.fundraising.event;

import org.example.fundraising.event.dto.EventReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Query( "SELECT e.name AS eventName, " +
            "e.balance AS amount, " +
            "e.currency AS currency " +
            "FROM EventEntity e")
    Set<EventReportProjection> getFinancialReport();
}
