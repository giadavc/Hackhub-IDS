package org.example.hackhubids.Controller;


import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Domain.TeamInvitation;
import org.example.hackhubids.Domain.TeamMembership;
import org.example.hackhubids.Service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        Team team = teamService.createTeam(request.getName(), request.getUserId());
        return ResponseEntity.ok(team);
    }

    @PostMapping("/{teamId}/invite")
    public ResponseEntity<TeamInvitation> inviteUser(@PathVariable Long teamId,
                                                     @Valid @RequestBody InviteUserRequest request) {
        TeamInvitation invitation = teamService.inviteUser(teamId, request.getInviterUserId(), request.getInvitedUserId());
        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/invitations/{invitationId}/accept")
    public ResponseEntity<TeamMembership> acceptInvitation(@PathVariable Long invitationId,
                                                           @Valid @RequestBody AcceptInvitationRequest request) {
        TeamMembership membership = teamService.acceptInvitation(invitationId, request.getInvitedUserId());
        return ResponseEntity.ok(membership);
    }

    @PostMapping("/invitations/{invitationId}/reject")
    public ResponseEntity<TeamInvitation> rejectInvitation(@PathVariable Long invitationId, @Valid @RequestBody RejectInvitationRequest request) {
    TeamInvitation invitation = teamService.rejectInvitation(invitationId, request.getInvitedUserId());
    return ResponseEntity.ok(invitation);
}

    @Data
    public static class CreateTeamRequest {
        private String name;
        private Long userId;
    }
    @Data
    public static class InviteUserRequest {
        private Long inviterUserId;
        private Long invitedUserId;
    }

    @Data
    public static class AcceptInvitationRequest {
        private Long invitedUserId;
    }

    @Data
    public static class RejectInvitationRequest {
        private Long invitedUserId;
    }
}

