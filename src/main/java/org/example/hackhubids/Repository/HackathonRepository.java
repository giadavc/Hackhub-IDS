package org.example.hackhubids.Repository;


import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HackathonRepository extends JpaRepository<Hackathon, Long> {

    List<Hackathon> findByStatus(HackathonStatus status);
}
