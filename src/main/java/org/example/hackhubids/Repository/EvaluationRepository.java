package org.example.hackhubids.Repository;

import java.util.Optional;

import org.example.hackhubids.Domain.Evaluation;
import org.example.hackhubids.Domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    
        Optional<Evaluation> findBySubmission(Submission submission);

}
