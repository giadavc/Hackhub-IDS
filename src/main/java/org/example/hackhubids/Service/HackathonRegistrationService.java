package org.example.hackhubids.Service;


import lombok.RequiredArgsConstructor;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonRegistration;
import org.example.hackhubids.Domain.HackathonStatus;
import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Repository.HackathonRegistrationRepository;
import org.example.hackhubids.Repository.HackathonRepository;
import org.example.hackhubids.Repository.TeamMembershipRepository;
import org.example.hackhubids.Repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HackathonRegistrationService {

    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final HackathonRegistrationRepository registrationRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    @Transactional
    public HackathonRegistration registerTeam(Long hackathonId, Long teamId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow();
        Team team = teamRepository.findById(teamId).orElseThrow();

        validateRegistrationWindow(hackathon);
        validateTeamSize(hackathon, team);
        validateMaxTeams(hackathon);

        if (registrationRepository.existsByHackathonAndTeam(hackathon, team)) {
            throw new IllegalStateException("Team already registered for this hackathon");
        }

        HackathonRegistration registration = HackathonRegistration.builder()
                .hackathon(hackathon)
                .team(team)
                .registeredAt(LocalDateTime.now())
                .build();

        return registrationRepository.save(registration);
    }

    private void validateRegistrationWindow(Hackathon hackathon) {
        LocalDate today = LocalDate.now();
        if (hackathon.getStatus() != HackathonStatus.IN_REGISTRATION) {
            throw new IllegalStateException("Hackathon is not open for registration");
        }
        if (hackathon.getRegistrationDeadline() != null
                && today.isAfter(hackathon.getRegistrationDeadline())) {
            throw new IllegalStateException("Registration deadline has passed");
        }
    }

    private void validateTeamSize(Hackathon hackathon, Team team) {
        long size = teamMembershipRepository.countByTeam(team);
        if (size > hackathon.getMaxTeamSize()) {
            throw new IllegalStateException("Team exceeds max team size for this hackathon");
        }
    }

    private void validateMaxTeams(Hackathon hackathon) {
        if (hackathon.getMaxTeams() > 0) {
            long registeredTeams = registrationRepository.countByHackathon(hackathon);
            if (registeredTeams >= hackathon.getMaxTeams()) {
                throw new IllegalStateException("Maximum number of teams for this hackathon reached");
            }
        }
    }
}
