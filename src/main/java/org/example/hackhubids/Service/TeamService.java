package org.example.hackhubids.Service;

import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Domain.TeamMembership;
import org.example.hackhubids.Repository.TeamMembershipRepository;
import org.example.hackhubids.Repository.TeamRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMembershipRepository teamMembershipRepository;

    @Transactional
    public Team createTeam(String teamName, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        teamMembershipRepository.findByUser(user).ifPresent(m -> {
            throw new IllegalStateException("User already belongs to a team");
        });

        Team team = Team.builder()
                .name(teamName)
                .build();
        team = teamRepository.save(team);

        TeamMembership membership = TeamMembership.builder()
                .team(team)
                .user(user)
                .joinDate(LocalDateTime.now())
                .build();
        teamMembershipRepository.save(membership);

        return team;
    }
}