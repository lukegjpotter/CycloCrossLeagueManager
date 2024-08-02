package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class BookingReportRepository {

    private final GoogleSheetsService googleSheetsService;
    private final GoogleSheetsSchemaService googleSheetsSchemaService;

    public BookingReportRepository(GoogleSheetsService googleSheetsService, GoogleSheetsSchemaService googleSheetsSchemaService) {
        this.googleSheetsService = googleSheetsService;
        this.googleSheetsSchemaService = googleSheetsSchemaService;
    }

    public List<BookingReportRowRecord> getDataFromSignUpsGoogleSheet(String signUpsGoogleSheetId) throws IOException {
        String sheetName = "Sheet 1";

        // Set the Indices of the important columns.
        List<String> spreadsheetHeaders = googleSheetsService.getSpreadsheetHeaders(signUpsGoogleSheetId, sheetName);
        int raceCategoryIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().raceCategory());
        int firstNameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().firstName());
        int surnameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().surname());
        int clubIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().club());
        int teamIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().team());

        // Build the Range.
        StringBuilder range = new StringBuilder(sheetName).append("!");
        // ToDo: Resume here and Build the Range.

        // Read the Sheet.
        List<BookingReportRowRecord> signupsFromBookingReport = new ArrayList<>();

        ValueRange valueRange = googleSheetsService.readSpreadsheetValuesInRange(signUpsGoogleSheetId, range.toString());
        List<List<Object>> standingsValues = valueRange.getValues();

        standingsValues.forEach(values -> {
            String fullName = new StringBuilder(String.valueOf(values.get(firstNameIndex - 1)))
                    .append(" ")
                    .append(values.get(surnameIndex - 1)).toString();

            String teamName = String.valueOf(values.get(teamIndex - 1));
            String clubOrTeam = (!teamName.equals("null")) ? teamName : String.valueOf(values.get(clubIndex - 1));

            signupsFromBookingReport.add(
                    new BookingReportRowRecord(
                            String.valueOf(values.get(raceCategoryIndex - 1)),
                            fullName,
                            clubOrTeam));
        });


        // Maybe: Ensure Signups are sorted on Race Category and Full Name.
        signupsFromBookingReport.sort(Comparator
                .comparing(BookingReportRowRecord::raceCategory)
                .thenComparing(BookingReportRowRecord::fullName));

        return signupsFromBookingReport;
    }
}
