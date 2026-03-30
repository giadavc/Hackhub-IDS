package org.example.hackhubids.Controller;

import org.example.hackhubids.Domain.StaffMember;
import org.example.hackhubids.Domain.StaffRole;
import org.example.hackhubids.Service.StaffService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {
    
    private final StaffService staffService;

    @PostMapping("/assign-role")
    public ResponseEntity<StaffMember> assignRole(@Valid @RequestBody AssignRoleRequest request) {
        StaffRole role = StaffRole.valueOf(request.getRole());
        StaffMember member = staffService.assignRole(request.getUserId(), role);
        return ResponseEntity.ok(member);
    }

    @Data
    public static class AssignRoleRequest {
        private Long userId;
        /**
         * Ruolo: ORGANIZER, JUDGE, MENTOR
         */
        private String role;
    }
}
