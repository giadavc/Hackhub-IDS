package org.example.hackhubids.Repository;

import org.example.hackhubids.Domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}