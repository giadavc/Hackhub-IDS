package org.example.hackhubids.Service;

import java.time.LocalDateTime;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonRegistration;
import org.example.hackhubids.Domain.StaffMember;
import org.example.hackhubids.Domain.StaffRole;
import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Domain.ViolationEnum;
import org.example.hackhubids.Domain.ViolationReport;
import org.example.hackhubids.Repository.HackathonRegistrationRepository;
import org.example.hackhubids.Repository.HackathonRepository;
import org.example.hackhubids.Repository.StaffMemberRepository;
import org.example.hackhubids.Repository.TeamRepository;
import org.example.hackhubids.Repository.ViolationReportRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViolationService {

    private final ViolationReportRepository violationReportRepository;
    private final HackathonRepository hackathonRepository;
    private final HackathonRegistrationRepository hackathonRegistrationRepository;
    private final TeamRepository teamRepository;
    private final StaffMemberRepository staffMemberRepository;

    @Transactional
    public ViolationReport reportViolation(Long hackathonId, Long teamId, Long mentorId, String description) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();
        StaffMember mentor = staffMemberRepository.findById(mentorId).orElseThrow();

        if (mentor.getRole() != StaffRole.MENTOR) {
            throw new IllegalStateException("Only a mentor can report violations");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }

        ViolationReport report = ViolationReport.builder()
                .hackathon(hackathon)
                .team(team)
                .mentor(mentor)
                .description(description.trim())
                .status(ViolationEnum.OPEN)
                .reportedAt(LocalDateTime.now())
                .build();

        return violationReportRepository.save(report);
    }

    @Transactional
    public ViolationReport resolveViolation(Long violationId, Long organizerId, ViolationDecision decision) {
        ViolationReport report = violationReportRepository.findById(violationId).orElseThrow();
        StaffMember organizer = staffMemberRepository.findById(organizerId).orElseThrow();

        if (organizer.getRole() != StaffRole.ORGANIZER) {
            throw new IllegalStateException("Only an organizer can resolve violations");
        }
        if (report.getStatus() != ViolationEnum.OPEN) {
            throw new IllegalStateException("Violation already resolved");
        }

        if (decision == ViolationDecision.REMOVE_TEAM) {
            HackathonRegistration registration = hackathonRegistrationRepository
                    .findByHackathonAndTeam(report.getHackathon(), report.getTeam())
                    .orElseThrow(() -> new IllegalStateException("Team is not registered in this hackathon"));
            hackathonRegistrationRepository.delete(registration);
            report.setStatus(ViolationEnum.TEAM_REMOVED);
        } else if (decision == ViolationDecision.DISMISS) {
            report.setStatus(ViolationEnum.DISMISSED);
        } else {
            throw new IllegalArgumentException("Unsupported decision");
        }

        return violationReportRepository.save(report);
    }

    public enum ViolationDecision {
        REMOVE_TEAM,
        DISMISS
    }
}