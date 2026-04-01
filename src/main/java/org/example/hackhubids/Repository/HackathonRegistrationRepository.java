package org.example.hackhubids.Repository;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonRegistration;
import org.example.hackhubids.Domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HackathonRegistrationRepository extends JpaRepository<HackathonRegistration, Long> {

    boolean existsByHackathonAndTeam(Hackathon hackathon, Team team);

    long countByHackathon(Hackathon hackathon);
}
