package org.example.hackhubids.Repository;

import org.example.hackhubids.Domain.CallProposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallProposalRepository extends JpaRepository<CallProposal, Long> {
}
