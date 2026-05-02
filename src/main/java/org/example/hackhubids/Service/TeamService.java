package org.example.hackhubids.Service;

import org.example.hackhubids.Domain.InvitationStatus;
import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Domain.TeamInvitation;
import org.example.hackhubids.Domain.TeamMembership;
import org.example.hackhubids.Domain.User;
import org.example.hackhubids.Repository.StaffMemberRepository;
import org.example.hackhubids.Repository.TeamInvitationRepository;
import org.example.hackhubids.Repository.TeamMembershipRepository;
import org.example.hackhubids.Repository.TeamRepository;
import org.example.hackhubids.Repository.UserRepository;

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
    private final TeamInvitationRepository teamInvitationRepository;
    private final StaffMemberRepository staffMemberRepository;

    @Transactional
    public Team createTeam(String teamName, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        ensureNotStaff(user);

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

    @Transactional
    public TeamInvitation inviteUser(Long teamId, Long inviterUserId, Long invitedUserId) {
        Team team = teamRepository.findById(teamId).orElseThrow();
        User inviter = userRepository.findById(inviterUserId).orElseThrow();
        User invitedUser = userRepository.findById(invitedUserId).orElseThrow();
        ensureNotStaff(inviter);
        ensureNotStaff(invitedUser);

        if (inviter.getId().equals(invitedUser.getId())) {
            throw new IllegalStateException("You cannot invite yourself");
        }

        TeamMembership inviterMembership = teamMembershipRepository.findByUser(inviter)
                .orElseThrow(() -> new IllegalStateException("Inviter does not belong to any team"));

        if (!inviterMembership.getTeam().getId().equals(team.getId())) {
            throw new IllegalStateException("Inviter is not a member of this team");
        }

        teamMembershipRepository.findByUser(invitedUser).ifPresent(m -> {
            throw new IllegalStateException("Invited user already belongs to a team");
        });

        if (teamInvitationRepository.existsByTeamAndInvitedUserAndStatus(team, invitedUser, InvitationStatus.PENDING)) {
            throw new IllegalStateException("A pending invitation already exists for this user");
        }

        TeamInvitation invitation = TeamInvitation.builder()
                .team(team)
                .inviter(inviter)
                .invitedUser(invitedUser)
                .status(InvitationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return teamInvitationRepository.save(invitation);
    }

    @Transactional
    public TeamMembership acceptInvitation(Long invitationId, Long invitedUserId) {
        TeamInvitation invitation = teamInvitationRepository.findById(invitationId).orElseThrow();
        User invitedUser = userRepository.findById(invitedUserId).orElseThrow();

        if (!invitation.getInvitedUser().getId().equals(invitedUser.getId())) {
            throw new IllegalStateException("You cannot accept an invitation for another user");
        }

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new IllegalStateException("Invitation is not pending");
        }

        teamMembershipRepository.findByUser(invitedUser).ifPresent(m -> {
            throw new IllegalStateException("User already belongs to a team");
        });

        TeamMembership membership = TeamMembership.builder()
                .team(invitation.getTeam())
                .user(invitedUser)
                .joinDate(LocalDateTime.now())
                .build();
        TeamMembership savedMembership = teamMembershipRepository.save(membership);

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRespondedAt(LocalDateTime.now());
        teamInvitationRepository.save(invitation);

        return savedMembership;
    }

    @Transactional
    public TeamInvitation rejectInvitation(Long invitationId, Long invitedUserId) {
        TeamInvitation invitation = teamInvitationRepository.findById(invitationId).orElseThrow();
        User invitedUser = userRepository.findById(invitedUserId).orElseThrow();

        if (!invitation.getInvitedUser().getId().equals(invitedUser.getId())) {
        throw new IllegalStateException("You cannot reject an invitation for another user");
        }

        if (invitation.getStatus() != InvitationStatus.PENDING) {
        throw new IllegalStateException("Invitation is not pending");
        }

        invitation.setStatus(InvitationStatus.REJECTED);
        invitation.setRespondedAt(LocalDateTime.now());

        return teamInvitationRepository.save(invitation);
    }

    private void ensureNotStaff(User user) {
        // Controlla se l'utente e' parte dello staff
        if (staffMemberRepository.findByUser(user).isPresent()) {
            throw new IllegalStateException("Staff members cannot create or join teams");
        }
    }
}
