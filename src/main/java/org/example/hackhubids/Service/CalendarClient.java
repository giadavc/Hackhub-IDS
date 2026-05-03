package org.example.hackhubids.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class CalendarClient implements CalendarPort {

    /**
     * Simulazione in-memory delle call prenotate su sistema esterno.
     * La chiave è l'externalCalendarId restituito all'app.
     */
    private final Map<String, String> simulatedEvents = new ConcurrentHashMap<>();

    @Override
    public String createCallSlot(Long teamId, Long mentorId, String timeSlot) {
        try {
            LocalDateTime.parse(timeSlot, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid timeSlot format. Use ISO_LOCAL_DATE_TIME, e.g. 2026-03-15T10:00:00", e);
        }

        String externalId = "CAL-MOCK-" + UUID.randomUUID();
        String payload = "teamId=" + teamId + ",mentorId=" + mentorId + ",timeSlot=" + timeSlot;
        simulatedEvents.put(externalId, payload);
        return externalId;
    }
}
