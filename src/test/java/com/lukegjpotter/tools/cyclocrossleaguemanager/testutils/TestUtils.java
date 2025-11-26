package com.lukegjpotter.tools.cyclocrossleaguemanager.testutils;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.GriddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestUtils {

    @Autowired
    GoogleSheetsService googleSheetsService;
    @Autowired
    GriddingRepository griddingRepository;

    public void wipeGriddingSheet(String griddingGoogleSheetId) {

        griddingRepository.writeGriddingToGoogleSheet(
                new ArrayList<>(List.of(
                        new RiderGriddingPositionRecord("A-Race", 1, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 2, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 3, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 4, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 5, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 6, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 7, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 1, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 2, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 3, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 4, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 5, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 6, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 7, "", ""),
                        new RiderGriddingPositionRecord("Women", 1, "", ""),
                        new RiderGriddingPositionRecord("Women", 2, "", ""),
                        new RiderGriddingPositionRecord("Women", 3, "", ""),
                        new RiderGriddingPositionRecord("Women", 4, "", ""),
                        new RiderGriddingPositionRecord("Women", 5, "", ""),
                        new RiderGriddingPositionRecord("Women", 6, "", ""),
                        new RiderGriddingPositionRecord("Women", 7, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 5, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 6, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 7, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 8, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Male", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Male", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Male", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Male", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Male", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Male", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Male", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Male", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Female", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Female", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Female", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Female", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Female", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Female", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Female", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Female", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Female", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Female", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Female", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Female", 4, "", ""))),
                griddingGoogleSheetId);
    }

    public String aRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!B4:C27");
    }

    public String womensRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!E4:F27");
    }

    public String bRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!H4:I27");
    }

    private String griddingSheetAndRangeToString(String griddingGoogleSheetId, String range) throws IOException {
        StringBuilder raceGridding = new StringBuilder();

        ValueRange raceValueRangeResponse = googleSheetsService.readSpreadsheetValuesInRange(griddingGoogleSheetId, range);
        List<List<Object>> raceValues = raceValueRangeResponse.getValues();
        if (raceValues == null) return "";

        for (List<Object> row : raceValues) {
            if (row.isEmpty()) break;
            raceGridding.append(String.format("%s, %s\n", row.get(0), row.get(1)));
        }

        return raceGridding.toString();
    }

    public String u16BoysRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!B31:C38");
    }

    public String u16GirlsRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!E31:F38");
    }

    public String u14BoysRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!B42:C49");
    }

    public String u14GirlsRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!E42:F49");
    }

    public String u12BoysRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!H31:I38");
    }

    public String u12GirlsRaceGriddingToString(String griddingGoogleSheetId) throws IOException {
        return griddingSheetAndRangeToString(griddingGoogleSheetId, "Gridding!H42:I49");
    }
}
