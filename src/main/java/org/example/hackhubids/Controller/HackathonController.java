package org.example.hackhubids.Controller;

import java.time.LocalDate;
import java.util.List;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Service.HackathonRegistrationService;
import org.example.hackhubids.Service.HackathonService;
import org.example.hackhubids.Service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/hackathons")
@RequiredArgsConstructor
public class HackathonController {
    
    private final HackathonService hackathonService;
    private final PaymentService paymentService;
    private final HackathonRegistrationService registrationService;

    @GetMapping
    public ResponseEntity<List<Hackathon>> listPublicHackathons() {
        return ResponseEntity.ok(hackathonService.listPublicHackathons());
    }

    @PostMapping
    public ResponseEntity<Hackathon> createHackathon(@Valid @RequestBody CreateHackathonRequest request) {
        Hackathon hackathon = Hackathon.builder()
                .name(request.getName())
                .rules(request.getRules())
                .registrationDeadline(request.getRegistrationDeadline())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .location(request.getLocation())
                .prizeAmount(request.getPrizeAmount())
                .maxTeamSize(request.getMaxTeamSize())
                .maxTeams(request.getMaxTeams())
                .build();
        return ResponseEntity.ok(hackathonService.createHackathon(hackathon, request.getOrganizerStaffId()));
    }

    @PostMapping("/{id}/proclaim-winner")
    public ResponseEntity<Void> proclaimWinnerAndPay(@PathVariable Long id,
                                                     @RequestParam Long teamId) {
        hackathonService.proclaimWinner(id);
        paymentService.payWinner(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/register-team")
    public ResponseEntity<Void> registerTeam(@PathVariable Long id,
                                             @RequestParam Long teamId) {
        registrationService.registerTeam(id, teamId);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class CreateHackathonRequest {
        private String name;
        private String rules;
        private LocalDate registrationDeadline;
        private LocalDate startDate;
        private LocalDate endDate;
        private String location;
        private double prizeAmount;
        private int maxTeamSize;
        private int maxTeams;
        /**
         * Identificatore dello StaffMember con ruolo ORGANIZER
         * che sta creando l'hackathon.
         */
        private Long organizerStaffId;
    }
}
