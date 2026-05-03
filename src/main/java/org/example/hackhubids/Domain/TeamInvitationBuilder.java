package org.example.hackhubids.Domain;

import java.time.LocalDateTime;

public class TeamInvitationBuilder {
    private Long id;
    private Team team;
    private User inviter;
    private User invitedUser;
    private InvitationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;

    public TeamInvitationBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TeamInvitationBuilder team(Team team) {
        this.team = team;
        return this;
    }

    public TeamInvitationBuilder inviter(User inviter) {
        this.inviter = inviter;
        return this;
    }

    public TeamInvitationBuilder invitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
        return this;
    }

    public TeamInvitationBuilder status(InvitationStatus status) {
        this.status = status;
        return this;
    }

    public TeamInvitationBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public TeamInvitationBuilder respondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
        return this;
    }

    public TeamInvitation build() {
        return new TeamInvitation(id, team, inviter, invitedUser, status, createdAt, respondedAt);
    }
}