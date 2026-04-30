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
public class SupportRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Hackathon hackathon;

    @ManyToOne(optional = false)
    private Team team;

    @ManyToOne
    private StaffMember mentor;

    @Column(nullable = false, length = 2000)
    private String message;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
