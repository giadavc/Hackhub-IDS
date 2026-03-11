package org.example.hackhubids.Service;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.HackathonStatus;
import org.example.hackhubids.Repository.HackathonRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HackathonService {
    private final HackathonRepository hackathonRepository;
    private final StaffMemberRepository staffMemberRepository;

    @Transactional(readOnly = true)
    public List<Hackathon> listPublicHackathons() {
        return hackathonRepository.findByStatus(HackathonStatus.IN_REGISTRATION);
    }

    @Transactional
    public Hackathon createHackathon(Hackathon hackathon, Long organizerStaffId) {
        StaffMember staffMember = staffMemberRepository.findById(organizerStaffId)
                .orElseThrow();
        if (staffMember.getRole() != StaffRole.ORGANIZER) {
            throw new IllegalStateException("Only an organizer can create a hackathon");
        }

        hackathon.setStatus(HackathonStatus.IN_REGISTRATION);
        return hackathonRepository.save(hackathon);
    }

    @Transactional
    public Hackathon proclaimWinner(Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow();
        hackathon.setStatus(HackathonStatus.FINISHED);
        return hackathonRepository.save(hackathon);
    }
    
}
