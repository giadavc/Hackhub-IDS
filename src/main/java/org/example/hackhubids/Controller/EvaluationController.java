package org.example.hackhubids.Controller;

import org.example.hackhubids.Domain.Evaluation;
import org.example.hackhubids.Service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<Evaluation> evaluate(@Valid @RequestBody EvaluateRequest request) {
        Evaluation evaluation = evaluationService.evaluateSubmission(
                request.getSubmissionId(),
                request.getJudgeId(),
                request.getScore(),
                request.getComment()
        );
        return ResponseEntity.ok(evaluation);
    }

    @Data
    public static class EvaluateRequest {
        private Long submissionId;
        private Long judgeId;
        private int score;
        private String comment;
    }
}
