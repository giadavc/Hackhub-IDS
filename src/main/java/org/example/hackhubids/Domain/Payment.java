package org.example.hackhubids.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Hackathon hackathon;

    @ManyToOne(optional = false)
    private Team team;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String status;

    private String externalTransactionId;

    private LocalDateTime paidAt;
}