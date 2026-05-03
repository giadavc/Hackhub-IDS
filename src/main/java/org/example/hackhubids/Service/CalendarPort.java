package org.example.hackhubids.Service;

public interface CalendarPort {

    String createCallSlot(Long teamId, Long mentorId, String timeSlot);
}
