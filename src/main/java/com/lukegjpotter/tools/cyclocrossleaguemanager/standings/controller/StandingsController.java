package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto.UpdateStandingsRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.service.StandingsUpdaterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StandingsController {

    private static final Logger logger = LoggerFactory.getLogger(StandingsController.class);
    private final StandingsUpdaterService standingsUpdaterService;

    @Autowired
    public StandingsController(StandingsUpdaterService standingsUpdaterService) {
        this.standingsUpdaterService = standingsUpdaterService;
    }

    @PostMapping("/updatestandings")
    public ResponseEntity<?> updateStandings(@Valid @RequestBody final UpdateStandingsRequestRecord updateStandingsRequestRecord) {
        logger.info("Update Standings called");
        logger.debug("Standings Update Request called with: {}", updateStandingsRequestRecord);

        return ResponseEntity.ok(standingsUpdaterService.updateStandings(updateStandingsRequestRecord));
    }
}
