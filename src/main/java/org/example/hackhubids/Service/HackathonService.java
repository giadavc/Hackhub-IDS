package org.example.hackhubids.Service;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonStaffAssignment;
import org.example.hackhubids.Domain.HackathonStatus;
import org.example.hackhubids.Domain.StaffMember;
import org.example.hackhubids.Domain.StaffRole;
import org.example.hackhubids.Domain.Submission;
import org.example.hackhubids.Repository.EvaluationRepository;
import org.example.hackhubids.Repository.HackathonRepository;
import org.example.hackhubids.Repository.HackathonStaffAssignmentRepository;
import org.example.hackhubids.Repository.StaffMemberRepository;
import org.example.hackhubids.Repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HackathonService {
    private final HackathonRepository hackathonRepository;
    private final StaffMemberRepository staffMemberRepository;
    private final SubmissionRepository submissionRepository;
    private final EvaluationRepository evaluationRepository;
    private final HackathonStaffAssignmentRepository hackathonStaffAssignmentRepository;

    @Transactional(readOnly = true)
    public List<Hackathon> listPublicHackathons() {
        return hackathonRepository.findByStatus(HackathonStatus.IN_REGISTRATION);
    }

    @Transactional(readOnly = true)
    public List<Submission> listSubmissions(Long hackathonId, Long staffMemberId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow();
        StaffMember staffMember = staffMemberRepository.findById(staffMemberId).orElseThrow();

        boolean allowed = hackathonStaffAssignmentRepository
                .existsByHackathonAndStaffMember(hackathon, staffMember);

        if (!allowed) {
            throw new IllegalStateException("Staff member is not assigned to this hackathon");
        }

        return submissionRepository.findByHackathon(hackathon);
    }  

    @Transactional
    public Hackathon createHackathon(Hackathon hackathon, Long organizerStaffId) {
        StaffMember staffMember = staffMemberRepository.findById(organizerStaffId)
                .orElseThrow();
        if (staffMember.getRole() != StaffRole.ORGANIZER) {
            throw new IllegalStateException("Only an organizer can create a hackathon");
        }

        hackathon.setStatus(HackathonStatus.IN_REGISTRATION);
        return hackathonRepository.save(hackathon);
    }

    @Transactional
    public HackathonStaffAssignment assignMentorToHackathon(Long hackathonId, Long organizerStaffId,Long mentorStaffId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow();
        StaffMember organizer = staffMemberRepository.findById(organizerStaffId).orElseThrow();
        StaffMember mentor = staffMemberRepository.findById(mentorStaffId).orElseThrow();

        if (organizer.getRole() != StaffRole.ORGANIZER) {
        throw new IllegalStateException("Only an organizer can assign mentors to a hackathon");
        }

        if (mentor.getRole() != StaffRole.MENTOR) {
        throw new IllegalStateException("Selected staff member is not a mentor");
        }

        boolean alreadyAssigned = hackathonStaffAssignmentRepository
        .existsByHackathonAndStaffMember(hackathon, mentor);

        if (alreadyAssigned) {
        throw new IllegalStateException("Mentor is already assigned to this hackathon");
        }

        HackathonStaffAssignment assignment = HackathonStaffAssignment.builder()
            .hackathon(hackathon)
            .staffMember(mentor)
            .build();

        return hackathonStaffAssignmentRepository.save(assignment);
    }

    @Transactional
    public Hackathon proclaimWinner(Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow();

        List<Submission> submissions = submissionRepository.findByHackathon(hackathon);
        if (submissions.isEmpty()) {
            throw new IllegalStateException("No submissions found for this hackathon");
        }

        Map<Long, Double> scoreByTeam = submissions.stream()
        .map(submission -> evaluationRepository.findBySubmission(submission)
                .map(evaluation -> Map.entry(submission.getTeam().getId(), evaluation.getScore())))
        .flatMap(java.util.Optional::stream)
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));

        if (scoreByTeam.isEmpty()) {
            throw new IllegalStateException("No evaluated submissions found for this hackathon");
        }

        double maxScore = scoreByTeam.values().stream()
                .max(Comparator.naturalOrder())
                .orElseThrow();

        List<Long> winnerCandidates = scoreByTeam.entrySet().stream()
                .filter(e -> Double.compare(e.getValue(), maxScore) == 0)
                .map(Map.Entry::getKey)
                .toList();

        if (winnerCandidates.size() > 1) {
            throw new IllegalStateException("Tie detected: multiple teams have the same top score");
        }

        Long winnerTeamId = winnerCandidates.get(0);
        hackathon.setWinnerTeamId(winnerTeamId);
        hackathon.setStatus(HackathonStatus.FINISHED);

        return hackathonRepository.save(hackathon);
    }
    
}
