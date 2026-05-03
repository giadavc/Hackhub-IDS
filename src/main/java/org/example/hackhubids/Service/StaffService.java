package org.example.hackhubids.Service;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonStatus;
import org.example.hackhubids.Domain.StaffMember;
import org.example.hackhubids.Domain.StaffRole;
import org.example.hackhubids.Domain.User;
import org.example.hackhubids.Repository.StaffMemberRepository;
import org.example.hackhubids.Repository.UserRepository;
import org.example.hackhubids.Repository.HackathonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StaffService {
    
    private final UserRepository userRepository;
    private final StaffMemberRepository staffMemberRepository;
    private final HackathonRepository hackathonRepository;

    @Transactional
    public StaffMember assignRole(Long userId, StaffRole role) {
        User user = userRepository.findById(userId).orElseThrow();
        StaffMember staff = staffMemberRepository.findByUser(user)
                .orElse(StaffMember.builder()
                        .user(user)
                        .role(role)
                        .build());
        staff.setRole(role);
        return staffMemberRepository.save(staff);
    }
    
    // Metodo illustartivo per invio sottomissioni di un team.
    @Transactional
    public Hackathon changeHackathonStatus(Long hackathonId, Long organizerId, HackathonStatus newStatus) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato con ID: " + hackathonId));

        hackathon.setStatus(newStatus);

        return hackathonRepository.save(hackathon);
    }
}
