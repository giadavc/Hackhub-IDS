package org.example.hackhubids.Domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_hackathon_staff_assignment",
                columnNames = {"hackathon_id", "staff_member_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HackathonStaffAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hackathon_id", nullable = false)
    private Hackathon hackathon;

    @ManyToOne(optional = false)
    @JoinColumn(name = "staff_member_id", nullable = false)
    private StaffMember staffMember;
}
