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

        logger.info("Getting Data from Sign Ups Google Sheet.");
        String sheetName = "Report";

        // Set the Indices of the important columns.
        List<String> spreadsheetHeaders = googleSheetsService.getSpreadsheetHeaders(signUpsGoogleSheetId, sheetName);
        int raceCategoryIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().raceCategory());
        int firstNameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().firstName());
        int surnameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().surname());
        int genderIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().gender());
        int clubIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().club());
        // ToDo: Remove Team, as it's been removed by the Booking Report.
        int teamIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.bookingReportHeaders().club());

        List<Integer> indices = List.of(raceCategoryIndex, firstNameIndex, surnameIndex, genderIndex, clubIndex, teamIndex);
        int minIndex = indices.stream().min(Integer::compareTo).get();
        int maxIndex = indices.stream().max(Integer::compareTo).get();

        // Build the Range.
        StringBuilder range = new StringBuilder(sheetName).append("!");
        List<String> alphabet = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH");
        range.append(alphabet.get(minIndex)).append("2:").append(alphabet.get(maxIndex));

        // Read the Sheet.
        List<BookingReportRowRecord> signupsFromBookingReport = new ArrayList<>();

        ValueRange valueRange = googleSheetsService.readSpreadsheetValuesInRange(signUpsGoogleSheetId, range.toString());
        List<List<Object>> bookingReportValues = valueRange.getValues();

        bookingReportValues.forEach(values -> {
            String fullName = values.get(firstNameIndex - raceCategoryIndex).toString().trim() + " " + values.get(surnameIndex - raceCategoryIndex).toString().trim();

            // Differentiate between Male and Female Underage riders.
            // Tickets could be called "Under 16" or "U16".
            String gender = String.valueOf(values.get(genderIndex - raceCategoryIndex)).trim();
            // Remove the "(non licence)" suffix.
            String ticketType = values.get(0).toString()
                    .replace(" (non licence)", "")
                    .replace(" (non-licence)", "")
                    .replace(" (leisure licence)", "")
                    .replace(" Fun race", "")
                    .trim();
            // Change "Youth U12 (2014-2015)" to "Under 12s".
            if (ticketType.startsWith("Youth")) {
                ticketType = ticketType.replace("Youth ", "").substring(0, 3).trim();
            }
            // raceCategory or ticketType need to be renamed from "A race..." to "A-Race", and "B race..." to "B-Race".
            ticketType = ticketType
                    .replace("A race", "A-Race")
                    .replace("A Race", "A-Race")
                    .replace("B race", "B-Race")
                    .replace("B Race", "B-Race")
                    .replace("a race", "A-Race")
                    .replace("a Race", "A-Race")
                    .replace("b race", "B-Race")
                    .replace("b Race", "B-Race")
                    .trim();
            String raceCategory = ((ticketType.startsWith("U")) ? ticketType + " " + gender : ticketType).trim();

            String clubOrTeam;
            try {
                String teamName = "";
                if (values.size() == 27) teamName = String.valueOf(values.get(teamIndex - raceCategoryIndex));
                clubOrTeam = (!teamName.isEmpty() && !teamName.equals("null")) ? teamName.trim() : String.valueOf(values.get(clubIndex - raceCategoryIndex)).trim();
            } catch (IndexOutOfBoundsException e) {
                clubOrTeam = "Un-Attached";
            }

            logger.trace("Booking Report Row: {}.", new BookingReportRowRecord(raceCategory, fullName, clubOrTeam));
            signupsFromBookingReport.add(new BookingReportRowRecord(raceCategory, fullName, clubOrTeam));
        });

        // Ensure Signups are sorted on Race Category and Full Name.
        if (isOutputSorted) {
            signupsFromBookingReport.sort(Comparator
                    .comparing(BookingReportRowRecord::raceCategory)
                    .thenComparing(BookingReportRowRecord::fullName));
        }

        logger.info("Number of Signups: {}.", signupsFromBookingReport.size());

        return signupsFromBookingReport;
    }
}
