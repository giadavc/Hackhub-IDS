package org.example.hackhubids.Repository;

import java.util.Collection;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonRegistration;
import org.example.hackhubids.Domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.hackhubids.Domain.HackathonStatus;

public interface HackathonRegistrationRepository extends JpaRepository<HackathonRegistration, Long> {

    boolean existsByTeamAndHackathon_StatusIn(Team team, Collection<HackathonStatus> statuses);

    long countByHackathon(Hackathon hackathon);
}
