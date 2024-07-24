package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingResultRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.service.GriddingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GriddingController {

    private static final Logger logger = LoggerFactory.getLogger(GriddingController.class);
    private final GriddingService griddingService;

    public GriddingController(GriddingService griddingService) {
        this.griddingService = griddingService;
    }

    @PostMapping("/gridding")
    public ResponseEntity<GriddingResultRecord> gridding(@RequestBody GriddingRequestRecord griddingRequestRecord) {

        GriddingResultRecord griddingResult = griddingService.gridSignups(griddingRequestRecord);

        if (griddingResult.errorMessage().isEmpty())
            return ResponseEntity.ok(griddingResult);

        return ResponseEntity.internalServerError().body(griddingResult);
    }
}
