package org.example.hackhubids.Repository;


import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonStaffAssignment;  
import org.example.hackhubids.Domain.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HackathonStaffAssignmentRepository extends JpaRepository<HackathonStaffAssignment, Long> {

    boolean existsByHackathonAndStaffMember(Hackathon hackathon, StaffMember staffMember);

    Optional<HackathonStaffAssignment> findByHackathonAndStaffMember(Hackathon hackathon, StaffMember staffMember);

    List<HackathonStaffAssignment> findByHackathon(Hackathon hackathon);

    List<HackathonStaffAssignment> findByStaffMember(StaffMember staffMember);
}
