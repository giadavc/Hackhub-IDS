package org.example.hackhubids.Repository;


import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    Optional<Submission> findByHackathonAndTeam(Hackathon hackathon, Team team);

    List<Submission> findByHackathon(Hackathon hackathon);
}


