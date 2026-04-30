package org.example.hackhubids.Repository;

import java.util.List;

import org.example.hackhubids.Domain.StaffMember;
import org.example.hackhubids.Domain.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {
    
    List<SupportRequest> findByMentor(StaffMember mentor);
    
}
