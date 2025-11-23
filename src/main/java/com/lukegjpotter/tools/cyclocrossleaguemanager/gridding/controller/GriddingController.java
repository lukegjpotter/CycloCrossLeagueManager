package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.service.GriddingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GriddingController {

    private static final Logger logger = LoggerFactory.getLogger(GriddingController.class);
    private final GriddingService griddingService;

    @Autowired
    public GriddingController(GriddingService griddingService) {
        this.griddingService = griddingService;
    }

    @PostMapping("/gridding")
    public ResponseEntity<?> gridding(@Valid @RequestBody final GriddingRequestRecord griddingRequestRecord) {
        logger.info("Gridding called.");
        logger.debug("Grid Signups Called with: {}", griddingRequestRecord);

        // ToDo: If Gridding Sheet is not set, create sheet.
        // ToDo: Handle Championship Event.
        return ResponseEntity.ok(griddingService.gridSignups(griddingRequestRecord));
    }
}
