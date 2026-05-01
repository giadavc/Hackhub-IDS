package org.example.hackhubids.Repository;

import org.example.hackhubids.Domain.InvitationStatus;
import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Domain.TeamInvitation;
import org.example.hackhubids.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation, Long> {
    
    boolean existsByTeamAndInvitedUserAndStatus(Team team, User invitedUser, InvitationStatus status);

}
