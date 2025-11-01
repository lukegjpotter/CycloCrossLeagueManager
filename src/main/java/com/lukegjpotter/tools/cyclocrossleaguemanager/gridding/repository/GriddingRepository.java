package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.GriddingRaceType;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingResultRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class GriddingRepository {

    private static final Logger logger = LoggerFactory.getLogger(GriddingRepository.class);
    private final GoogleSheetsSchemaService googleSheetsSchemaService;
    private final GoogleSheetsService googleSheetsService;

    public GriddingRepository(GoogleSheetsSchemaService googleSheetsSchemaService, GoogleSheetsService googleSheetsService) {
        this.googleSheetsSchemaService = googleSheetsSchemaService;
        this.googleSheetsService = googleSheetsService;
    }

    public GriddingResultRecord writeGriddingToGoogleSheet(List<RiderGriddingPositionRecord> ridersInGriddedOrder, final String griddingGoogleSheet) {

        // fixme: Not printing Underage or Women Masters.
        logger.info("Writing Gridding to Google Sheet.");

        // Convert Gridding Sheet to URL, and extract Sheet ID.
        String griddingGoogleSheetId, griddingGoogleSheetUrlStringWithoutQueryString;
        try {
            URL griddingGoogleSheetURL = new URL(griddingGoogleSheet);
            griddingGoogleSheetId = griddingGoogleSheetURL.getPath().split("/")[3];
            // Strip query string from griddingGoogleSheet.
            String[] urlPath = griddingGoogleSheetURL.getPath().split("/");
            griddingGoogleSheetUrlStringWithoutQueryString = griddingGoogleSheetURL.getProtocol() + "://"
                    + griddingGoogleSheetURL.getHost() + "/"
                    + urlPath[1] + "/"
                    + urlPath[2] + "/"
                    + urlPath[3] + "/";
        } catch (MalformedURLException e) {
            return new GriddingResultRecord(griddingGoogleSheet, "Malformed URL Exception: " + e.getMessage());
        }

        logger.trace("Riders in Gridded Order Size: {}.", ridersInGriddedOrder.size());
        logger.trace("Riders in Gridded order contains: {}", ridersInGriddedOrder.subList(0, 5));

        // Sort the sheet, so common races are grouped, and gridding is in order.
        ridersInGriddedOrder.sort(Comparator
                .comparing(RiderGriddingPositionRecord::raceCategory)
                .thenComparingInt(RiderGriddingPositionRecord::gridPosition));

        // Write ridersInGriddedOrder to Google Sheet.
        List<GriddingRaceType> griddingSchema = googleSheetsSchemaService.griddingSchema();

        try {
            for (GriddingRaceType griddingRaceType : griddingSchema) {
                List<RiderGriddingPositionRecord> ridersInRace = ridersInGriddedOrder.stream()
                        .filter(riderGriddingPositionRecord -> riderGriddingPositionRecord.raceCategory().equals(griddingRaceType.raceCategory()))
                        .limit(griddingRaceType.maxRidersToGrid())
                        .toList();

                List<List<Object>> ridersAndClubs = new ArrayList<>();
                for (RiderGriddingPositionRecord rider : ridersInRace) {
                    ridersAndClubs.add(List.of(rider.fullName(), rider.clubName()));
                }

                googleSheetsService.writeValuesToSpreadsheetFromCell(
                        griddingGoogleSheetId,
                        griddingRaceType.startCell(),
                        new ValueRange().setValues(ridersAndClubs));
            }
        } catch (IOException e) {
            return new GriddingResultRecord(griddingGoogleSheetUrlStringWithoutQueryString, "IO Exception: " + e.getMessage());
        }

        logger.info("Gridding written to Google Sheet.");
        return new GriddingResultRecord(griddingGoogleSheetUrlStringWithoutQueryString, "");
    }
}
