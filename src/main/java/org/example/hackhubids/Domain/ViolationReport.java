package org.example.hackhubids.Domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Hackathon hackathon;

    @ManyToOne(optional = false)
    private Team team;

    @ManyToOne(optional = false)
    private StaffMember mentor;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private ViolationEnum status;

    @Column(nullable = false)
    private LocalDateTime reportedAt;
}