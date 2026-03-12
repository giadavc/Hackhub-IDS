package org.example.hackhubids.Controller;


import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.example.hackhubids.Domain.Team;
import org.example.hackhubids.Service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@Valid @RequestBody CreateTeamRequest request) {
        Team team = teamService.createTeam(request.getName(), request.getUserId());
        return ResponseEntity.ok(team);
    }

    @Data
    public static class CreateTeamRequest {
        private String name;
        private Long userId;
    }
}

