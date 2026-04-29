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
public class CallProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private SupportRequest supportRequest;

    @ManyToOne(optional = false)
    private StaffMember mentor;

    @Column(nullable = false)
    private String externalCalendarId;

    @Column(nullable = false)
    private LocalDateTime proposedAt;
}
