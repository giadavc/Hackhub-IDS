package org.example.hackhubids.Service;

import org.example.hackhubids.Domain.Hackathon;
import org.example.hackhubids.Domain.Payment;
import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Repository.HackathonRepository;
import org.example.hackhubids.Repository.PaymentRepository;
import org.example.hackhubids.Repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentProvider paymentProvider;

    @Transactional
    public Payment payWinner(Long hackathonId){
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow();
        Team team = teamRepository.findById(hackathon.getWinnerTeamId())
                .orElseThrow();
        
        PaymentResult paymentResult = paymentProvider.pay(team.getId(), hackathon.getPrizeAmount());

        Payment payment = Payment.builder()
                .hackathon(hackathon)
                .team(team)
                .amount(hackathon.getPrizeAmount())
                .status(paymentResult.isSuccess() ? "PAD" : "FAILED")
                .externalTransactionId(paymentResult.getExternalTransactionId())
                .paidAt(paymentResult.isSuccess() ? LocalDateTime.now() : null)
                .build();

        return paymentRepository.save(payment);
    }
}
