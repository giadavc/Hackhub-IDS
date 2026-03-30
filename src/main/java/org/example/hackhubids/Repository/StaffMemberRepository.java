package org.example.hackhubids.Repository;

import org.example.hackhubids.Domain.StaffMember;
import org.example.hackhubids.Domain.StaffRole;
import org.example.hackhubids.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffMemberRepository extends JpaRepository<StaffMember, Long> {
    
    Optional<StaffMember> findByUser(User user);

    List<StaffMember> findByRole(StaffRole role);
}
