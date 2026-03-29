package org.example.hackhubids.Repository;

import org.example.hackhubids.Domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}