package org.example.hackhubids.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hackathon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 4000)
    private String rules;

    private LocalDate registrationDeadline;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private double prizeAmount;
    private int maxTeamSize;
    private Long winnerTeamId;

    /**
     * Numero massimo di team iscrivibili all'hackathon.
     * Se 0 o negativo, nessun limite viene applicato.
     */
    private int maxTeams;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private HackathonStatus status = HackathonStatus.IN_REGISTRATION;
    
}
