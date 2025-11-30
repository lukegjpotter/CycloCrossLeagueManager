package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.testutils;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.RiderNameAndCellRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.testutils.model.StringAndIntegerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StandingsTestUtils {

    @Autowired
    GoogleSheetsService googleSheetsService;
    @Autowired
    GoogleSheetsSchemaService googleSheetsSchemaService;

    public void wipeStandingsSheetResultsColumnAndNames(String standingsSheetId, String resultsColumn, List<RiderNameAndCellRecord> riderNamesToWipe) throws IOException {

        googleSheetsSchemaService.leagueStandingsSchema().forEach(raceType -> {
            // Wipe Column.
            int numberOfRows = 0;
            try {
                numberOfRows = googleSheetsService.readSpreadsheetValuesInRange(standingsSheetId, raceType.raceType() + "!" + resultsColumn + "2:" + resultsColumn).getValues().size();
            } catch (IOException e) {
                throw new RuntimeException("Error Wiping sheet, when Reading Spreadsheet Values in Range", e);
            }

            List<List<Object>> zeroValues = new ArrayList<>();
            for (int i = 0; i < numberOfRows; i++) {
                zeroValues.add(List.of(0));
            }

            try {
                googleSheetsService.writeValuesToSpreadsheetFromCell(standingsSheetId, raceType.raceType() + "!" + resultsColumn + "2:" + resultsColumn, new ValueRange().setValues(zeroValues));
            } catch (IOException e) {
                throw new RuntimeException("Error Wiping sheet, when Reading Spreadsheet Values in Range", e);
            }

            // Wipe Names.
            List<StringAndIntegerRecord> sheetAndRowNumbers = new ArrayList<>();
            List<List<Object>> standingsNames = null;
            try {
                standingsNames = googleSheetsService.readSpreadsheetValuesInRange(standingsSheetId, raceType.raceType() + "!B2:C").getValues();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (int row = 0; row < standingsNames.size(); row++) {
                List<Object> rowValues = standingsNames.get(row);
                String name = rowValues.get(0).toString().trim();
                String club = rowValues.get(1).toString().trim();

                if (name.equals("Test Rider")) {
                    int i = 0;
                }

                if (riderNamesToWipe.contains(new RiderNameAndCellRecord(raceType.raceType(), name, club, ""))) {
                    sheetAndRowNumbers.add(new StringAndIntegerRecord(raceType.raceType(), row + 2));
                }
            }

            sheetAndRowNumbers.forEach(sheetAndRowNumber -> {
                try {
                    googleSheetsService.writeValuesToSpreadsheetFromCell(standingsSheetId, sheetAndRowNumber.raceType() + "!B" + sheetAndRowNumber.rowNumber() + ":D" + sheetAndRowNumber.rowNumber(), new ValueRange().setValues(List.of(List.of("", "", ""))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        // ToDo: Sort Sheet.
    }

    public String aRaceStandingsToString(String standingsSheetId) throws IOException {
        return standingsSheetAndRangeToString(standingsSheetId, "A-Race!A2:K");
    }

    public String womensRaceStandingsToString(String standingsSheetId) throws IOException {
        return standingsSheetAndRangeToString(standingsSheetId, "Women!A2:k");
    }

    private String standingsSheetAndRangeToString(String standingsGoogleSheetId, String range) throws IOException {
        StringBuilder standings = new StringBuilder();

        ValueRange raceValueRangeResponse = googleSheetsService.readSpreadsheetValuesInRange(standingsGoogleSheetId, range);
        List<List<Object>> raceValues = raceValueRangeResponse.getValues();
        if (raceValues == null) return "";

        for (List<Object> row : raceValues) {
            if (row.isEmpty()) break;
            standings.append(String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s\n", row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6), row.get(7), row.get(8), row.get(9), row.get(10)));
        }

        return standings.toString();
    }
}
