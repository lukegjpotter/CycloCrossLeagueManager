package com.lukegjpotter.tools.cyclocrossleaguemanager.common.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.ResultsAndStandingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResultsAndStandingsController {

    private static final Logger logger = LoggerFactory.getLogger(ResultsAndStandingsController.class);
    private final ResultsAndStandingsService resultsAndStandingsService;

    public ResultsAndStandingsController(ResultsAndStandingsService resultsAndStandingsService) {
        this.resultsAndStandingsService = resultsAndStandingsService;
    }

    @PostMapping("/updatestandings")
    public ResponseEntity<?> updateStandings(@RequestBody String raceResultsUrl) {
        logger.info("Update Standings called");
        logger.debug("Standings Update Request called with: {}", raceResultsUrl);

        try {
            return ResponseEntity.ok(resultsAndStandingsService.performETL(raceResultsUrl));
        } catch (Exception exception) {
            logger.error("Error");
        }

        // ToDo: Return Response Code and Status.
        return ResponseEntity.badRequest().body("\"errorMessage\":\"Error in Gridding\"");
    }
}
