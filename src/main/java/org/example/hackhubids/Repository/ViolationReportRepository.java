package org.example.hackhubids.Repository;

import org.example.hackhubids.Domain.ViolationReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViolationReportRepository extends JpaRepository<ViolationReport, Long> {
}

