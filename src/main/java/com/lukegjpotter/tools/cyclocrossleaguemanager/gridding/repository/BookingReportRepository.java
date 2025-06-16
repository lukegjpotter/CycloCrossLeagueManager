package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class BookingReportRepository {

    private static final Logger logger = LoggerFactory.getLogger(BookingReportRepository.class);
    private final GoogleSheetsService googleSheetsService;
    private final GoogleSheetsSchemaService googleSheetsSchemaService;

    public BookingReportRepository(GoogleSheetsService googleSheetsService, GoogleSheetsSchemaService googleSheetsSchemaService) {
        this.googleSheetsService = googleSheetsService;
        this.googleSheetsSchemaService = googleSheetsSchemaService;
    }

    public List<BookingReportRowRecord> getDataFromSignUpsGoogleSheet(String signUpsGoogleSheetId, boolean isOutputSorted) throws IOException {

        logger.trace("Getting Data from Sign Ups Google Sheet.");
        String sheetName = "Report";

        // Set the Indices of the important columns.
        List<String> spreadsheetHeaders = googleSheetsService.getSpreadsheetHeaders(signUpsGoogleSheetId, sheetName);
        int raceCategoryIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().raceCategory());
        int firstNameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().firstName());
        int surnameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().surname());
        int genderIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().gender());
        int clubIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().club());
        int teamIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().team());

        // Build the Range.
        StringBuilder range = new StringBuilder(sheetName).append("!");
        List<String> alphabet = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH");
        range.append(alphabet.get(raceCategoryIndex)).append("2:").append(alphabet.get(teamIndex));

        // Read the Sheet.
        List<BookingReportRowRecord> signupsFromBookingReport = new ArrayList<>();

        ValueRange valueRange = googleSheetsService.readSpreadsheetValuesInRange(signUpsGoogleSheetId, range.toString());
        List<List<Object>> bookingReportValues = valueRange.getValues();

        bookingReportValues.forEach(values -> {
            String fullName = values.get(firstNameIndex - raceCategoryIndex) + " " + values.get(surnameIndex - raceCategoryIndex);

            // Differentiate between Male and Female Underage riders.
            // Tickets could be called "Under 16" or "U16".
            String gender = String.valueOf(values.get(genderIndex - raceCategoryIndex));
            String ticketType = values.get(0).toString();
            String raceCategory = (ticketType.startsWith("U")) ? ticketType + " " + gender : ticketType;

            String teamName = "";
            if (values.size() == 27) teamName = String.valueOf(values.get(teamIndex - raceCategoryIndex));
            String clubOrTeam = (!teamName.isEmpty() && !teamName.equals("null")) ? teamName : String.valueOf(values.get(clubIndex - raceCategoryIndex));

            signupsFromBookingReport.add(new BookingReportRowRecord(raceCategory, fullName, clubOrTeam));
        });

        // Maybe: Ensure Signups are sorted on Race Category and Full Name.
        if (isOutputSorted) {
            signupsFromBookingReport.sort(Comparator
                    .comparing(BookingReportRowRecord::raceCategory)
                    .thenComparing(BookingReportRowRecord::fullName));
        }

        logger.trace("Number of Signups: {}.", signupsFromBookingReport.size());

        return signupsFromBookingReport;
    }
}
