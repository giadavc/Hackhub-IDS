package org.example.hackhubids.Service;

import lombok.RequiredArgsConstructor;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonStatus;
import org.example.hackhubids.Domain.Submission;
import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Repository.HackathonRepository;
import org.example.hackhubids.Repository.SubmissionRepository;
import org.example.hackhubids.Repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    
    private final SubmissionRepository submissionRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public Submission submit(Long hackathonId, Long teamId, String contentUrl) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        validateSubmissionWindow(hackathon);

        Submission submission = submissionRepository.findByHackathonAndTeam(hackathon, team)
                .orElse(Submission.builder()
                        .hackathon(hackathon)
                        .team(team)
                        .build());

        submission.setContentUrl(contentUrl);
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }

    private void validateSubmissionWindow(Hackathon hackathon) {
        LocalDate now = LocalDate.now();
        if (hackathon.getStatus() == HackathonStatus.FINISHED
                || hackathon.getStatus() == HackathonStatus.IN_EVALUATION
                || (hackathon.getEndDate() != null && now.isAfter(hackathon.getEndDate()))) {
            throw new IllegalStateException("Submission deadline has passed");
        }
    }
    
}
