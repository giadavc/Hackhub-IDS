package org.example.hackhubids.Service;

import org.example.hackhubids.Domain.Evaluation;
import org.example.hackhubids.Domain.StaffMember;
import org.example.hackhubids.Domain.Submission;
import org.example.hackhubids.Repository.EvaluationRepository;
import org.example.hackhubids.Repository.StaffMemberRepository;
import org.example.hackhubids.Repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final SubmissionRepository submissionRepository;
    private final StaffMemberRepository staffMemberRepository;

    @Transactional
    public Evaluation evaluateSubmission(Long submissionId, Long judgeId, double score, String comment) {
        if (score < 0 || score > 10) {
            throw new IllegalArgumentException("Score must be between 0.0 and 10.0");
        }

        Submission submission = submissionRepository.findById(submissionId).orElseThrow();
        StaffMember judge = staffMemberRepository.findById(judgeId).orElseThrow();

        Evaluation evaluation = evaluationRepository.findBySubmission(submission)
                .orElse(Evaluation.builder()
                        .submission(submission)
                        .judge(judge)
                        .build());

        evaluation.setScore(score);
        evaluation.setComment(comment);

        return evaluationRepository.save(evaluation);
    }
}
