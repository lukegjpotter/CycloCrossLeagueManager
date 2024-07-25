package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.component;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Component
public class SheetsQuickstart {

    private final GoogleSheetsService googleSheetsService;

    public SheetsQuickstart(GoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }

    /**
     * Prints the names and majors of students in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
     */
    public String namesAndMajors() throws IOException, GeneralSecurityException {
        final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        final String range = "Class Data!A2:E2";

        ValueRange response = googleSheetsService.getGoogleSheets().spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        List<List<Object>> values = response.getValues();
        StringBuilder namesAndMajors = new StringBuilder();
        if (values == null || values.isEmpty()) {
            return "No data found.";
        } else {
            namesAndMajors.append("Name, Major\n");

            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                namesAndMajors.append(String.format("%s, %s\n", row.get(0), row.get(4)));
            }
        }
        return namesAndMajors.toString();
    }
}
