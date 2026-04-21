package org.example.hackhubids.Controller;

import org.example.hackhubids.Domain.Submission;
import org.example.hackhubids.Service.SubmissionService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<Submission> submit(@Valid @RequestBody SubmitRequest request) {
        Submission submission = submissionService.submit(
                request.getHackathonId(),
                request.getTeamId(),
                request.getContentUrl()
        );
        return ResponseEntity.ok(submission);
    }

    @Data
    public static class SubmitRequest {
        private Long hackathonId;
        private Long teamId;
        private String contentUrl;
    }
}
