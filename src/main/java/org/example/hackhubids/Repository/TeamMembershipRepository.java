package org.example.hackhubids.Repository;


import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Domain.TeamMembership;
import org.example.hackhubids.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamMembershipRepository extends JpaRepository<TeamMembership, Long> {

    Optional<TeamMembership> findByUser(User user);

    long countByTeam(Team team);
}