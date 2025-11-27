package com.lukegjpotter.tools.cyclocrossleaguemanager.common.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.dto.HealthRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthRecord> health() {
        return ResponseEntity.ok(new HealthRecord("alive"));
    }
}
