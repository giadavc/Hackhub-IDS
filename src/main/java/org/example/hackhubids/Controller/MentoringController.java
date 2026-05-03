package org.example.hackhubids.Controller;

import java.util.List;

import org.example.hackhubids.Domain.CallProposal;
import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.SupportRequest;
import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Repository.HackathonRepository;
import org.example.hackhubids.Repository.TeamRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mentoring")
@RequiredArgsConstructor
public class MentoringController {

    private final MentoringService mentoringService;
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;

    @PostMapping("/support-requests")
    public ResponseEntity<SupportRequest> createSupportRequest(@Valid @RequestBody CreateSupportRequest request) {
        Hackathon hackathon = hackathonRepository.findById(request.getHackathonId()).orElseThrow();
        Team team = teamRepository.findById(request.getTeamId()).orElseThrow();

        SupportRequest entity = SupportRequest.builder()
                .hackathon(hackathon)
                .team(team)
                .message(request.getMessage())
                .status("OPEN")
                .build();

        return ResponseEntity.ok(mentoringService.createSupportRequest(entity));
    }

    @GetMapping("/support-requests/{mentorId}")
    public ResponseEntity<List<SupportRequest>> listSupportRequests(@PathVariable Long mentorId) {
        return ResponseEntity.ok(mentoringService.listSupportRequestsForMentor(mentorId));
    }

    @PostMapping("/support-requests/{id}/propose-call")
    public ResponseEntity<CallProposal> proposeCall(@PathVariable Long id,
                                                    @Valid @RequestBody ProposeCallRequest request) {
        CallProposal proposal = mentoringService.proposeCall(id, request.getMentorId(), request.getTimeSlot());
        return ResponseEntity.ok(proposal);
    }

    @Data
    public static class CreateSupportRequest {
        private Long hackathonId;
        private Long teamId;
        private String message;
    }

    @Data
    public static class ProposeCallRequest {
        private Long mentorId;
        private String timeSlot;
    }
}
