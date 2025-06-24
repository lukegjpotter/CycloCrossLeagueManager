package com.lukegjpotter.tools.cyclocrossleaguemanager.testutils;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class TestUtils {

    @Autowired
    GoogleSheetsService googleSheetsService;

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
