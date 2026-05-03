package org.example.hackhubids.Controller;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.hackhubids.Domain.ViolationReport;
import org.example.hackhubids.Service.ViolationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/violations")
@RequiredArgsConstructor
public class ViolationController {

    private final ViolationService violationService;

    @PostMapping
    public ResponseEntity<ViolationReport> reportViolation(@Valid @RequestBody ReportViolationRequest request) {
        ViolationReport report = violationService.reportViolation(
                request.getHackathonId(),
                request.getTeamId(),
                request.getMentorId(),
                request.getDescription()
        );
        return ResponseEntity.ok(report);
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<ViolationReport> resolveViolation(@PathVariable Long id,
                                                            @Valid @RequestBody ResolveViolationRequest request) {
        ViolationService.ViolationDecision decision;
        try {
            decision = ViolationService.ViolationDecision.valueOf(request.getDecision().trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid decision. Use REMOVE_TEAM or DISMISS");
        }

        ViolationReport resolved = violationService.resolveViolation(id, request.getOrganizerId(), decision);
        return ResponseEntity.ok(resolved);
    }

    @Data
    public static class ReportViolationRequest {
        private Long hackathonId;
        private Long teamId;
        private Long mentorId;
        private String description;
    }

    @Data
    public static class ResolveViolationRequest {
        private Long organizerId;
        /**
         * Allowed values: REMOVE_TEAM, DISMISS
         */
        private String decision;
    }
}