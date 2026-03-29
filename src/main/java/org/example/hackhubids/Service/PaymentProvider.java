package org.example.hackhubids.Service;

public interface PaymentProvider {

    PaymentResult pay(Long teamId, Double amount);
}
