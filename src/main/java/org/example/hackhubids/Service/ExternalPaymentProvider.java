package org.example.hackhubids.Service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExternalPaymentProvider implements PaymentProvider {

    private static final double SUCCESS_RATE = 0.90;   // 90% successi

    @Override
    public PaymentResult pay(Long teamId, Double amount) {
        validateInput(teamId, amount);

        boolean success = ThreadLocalRandom.current().nextDouble() < SUCCESS_RATE;
        String transactionId = success ? generateTransactionId(teamId) : null;

        return new PaymentResult(success, transactionId);
    }

    private void validateInput(Long teamId, Double amount) {
        if (teamId == null || teamId <= 0) {
            throw new IllegalArgumentException("teamId non valido");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("amount non valido");
        }
    }
    private String generateTransactionId(Long teamId) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String shortUuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "SIM-" + ts + "-T" + teamId + "-" + shortUuid;
    }
}