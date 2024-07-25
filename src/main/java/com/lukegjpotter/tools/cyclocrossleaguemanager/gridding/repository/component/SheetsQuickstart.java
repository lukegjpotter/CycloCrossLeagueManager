package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.component;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SheetsQuickstart {

    private final GoogleSheetsService googleSheetsService;

    public SheetsQuickstart(GoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }

    public String namesAndMajors() throws IOException {
        final String googleSpreadSheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        final String range = "Class Data!A2:E2";

        ValueRange response = googleSheetsService.spreadsheetValuesInRange(googleSpreadSheetId, range);

        List<List<Object>> values = response.getValues();
        StringBuilder namesAndMajors = new StringBuilder("Name, Major\n");
        if (values == null || values.isEmpty()) {
            return "No data found.";
        } else {
            for (List<Object> row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                namesAndMajors.append(String.format("%s, %s\n", row.get(0), row.get(4)));
            }
        }
        return namesAndMajors.toString();
    }
}
