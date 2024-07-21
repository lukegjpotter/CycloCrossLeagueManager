package com.lukegjpotter.tools.cyclocrossleaguemanager.common.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.ResultsAndStandingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostMapping("/raceresults")
    public String raceResults(@RequestBody String raceResultsUrl) {
        resultsAndStandingsService.performETL(raceResultsUrl);

        // ToDo: Return Response Code and Status.
        return "";
    }
}
