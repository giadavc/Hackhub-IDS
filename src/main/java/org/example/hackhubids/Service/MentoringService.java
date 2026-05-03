package org.example.hackhubids.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.example.hackhubids.Domain.CallProposal;
import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.StaffMember;
import org.example.hackhubids.Domain.SupportRequest;
import org.example.hackhubids.Repository.CallProposalRepository;
import org.example.hackhubids.Repository.StaffMemberRepository;
import org.example.hackhubids.Repository.SupportRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentoringService {
    
    private final SupportRequestRepository supportRequestRepository;
    private final CallProposalRepository callProposalRepository;
    private final StaffMemberRepository staffMemberRepository;
    private final CalendarPort calendarPort;

    @Transactional
    public SupportRequest createSupportRequest(SupportRequest request) {
        request.setCreatedAt(LocalDateTime.now());
        request.setStatus("OPEN");
        return supportRequestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<SupportRequest> listSupportRequestsForMentor(Long mentorId) {
        StaffMember mentor = staffMemberRepository.findById(mentorId).orElseThrow();
        return supportRequestRepository.findByMentor(mentor);
    }

    @Transactional
    public CallProposal proposeCall(Long supportRequestId, Long mentorId, String timeSlot) {
        SupportRequest request = supportRequestRepository.findById(supportRequestId).orElseThrow();
        StaffMember mentor = staffMemberRepository.findById(mentorId).orElseThrow();
        LocalDateTime callDateTime = parseTimeSlot(timeSlot);
        validateCallWithinHackathonWindow(request.getHackathon(), callDateTime);

        String externalId = calendarPort.createCallSlot(request.getTeam().getId(), mentor.getId(), timeSlot);

        CallProposal proposal = CallProposal.builder()
                .supportRequest(request)
                .mentor(mentor)
                .externalCalendarId(externalId)
                .proposedAt(LocalDateTime.now())
                .build();

        return callProposalRepository.save(proposal);
    }

    private LocalDateTime parseTimeSlot(String timeSlot) {
        try {
            return LocalDateTime.parse(timeSlot, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid timeSlot format. Use ISO_LOCAL_DATE_TIME, e.g. 2026-03-15T10:00:00", e);
        }
    }

    private void validateCallWithinHackathonWindow(Hackathon hackathon, LocalDateTime callDateTime) {
        if (hackathon.getStartDate() != null) {
            LocalDateTime hackathonStart = hackathon.getStartDate().atStartOfDay();
            if (callDateTime.isBefore(hackathonStart)) {
                throw new IllegalStateException("Call time is before hackathon start date");
            }
        }
        if (hackathon.getEndDate() != null) {
            LocalDateTime hackathonEnd = LocalDateTime.of(hackathon.getEndDate(), LocalTime.MAX);
            if (callDateTime.isAfter(hackathonEnd)) {
                throw new IllegalStateException("Call time is after hackathon end date");
            }
        }
    }
}
