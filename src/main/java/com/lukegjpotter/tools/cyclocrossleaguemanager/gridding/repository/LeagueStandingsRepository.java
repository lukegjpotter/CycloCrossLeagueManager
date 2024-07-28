package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.LeagueStandingsRaceType;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class LeagueStandingsRepository {

    private final GoogleSheetsService googleSheetsService;
    private final GoogleSheetsSchemaService googleSheetsSchemaService;
    @Value("${gridding.currentseason.standings}")
    private String currentSeasonLeagueStandingsSpreadSheetId;
    @Value("${gridding.lastseason.standings}")
    private String lastSeasonLeagueStandingsSpreadSheetId;

    public LeagueStandingsRepository(GoogleSheetsService googleSheetsService, GoogleSheetsSchemaService googleSheetsSchemaService) {
        this.googleSheetsService = googleSheetsService;
        this.googleSheetsSchemaService = googleSheetsSchemaService;
    }

    public List<LeagueStandingsRowRecord> loadDataFromLeagueStandingsGoogleSheet(int roundNumber) throws IOException {

        // Decide which year's League Standings to use.
        String leagueStandingsSpreadSheetId = (roundNumber == 1) ?
                lastSeasonLeagueStandingsSpreadSheetId : currentSeasonLeagueStandingsSpreadSheetId;

        // Set the Indices of the .
        List<LeagueStandingsRaceType> leagueStandingsRaceTypes = googleSheetsSchemaService.leagueStandingsSchema();
        List<String> spreadsheetHeaders = googleSheetsService.getSpreadsheetHeaders(
                leagueStandingsSpreadSheetId, leagueStandingsRaceTypes.get(0).raceType());
        int fullNameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().fullName());
        int clubIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().club());
        int totalPointsIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().totalPoints());

        // Build the Range
        StringBuilder range = new StringBuilder("!");
        List<String> alphabet = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        range.append(alphabet.get(fullNameIndex)).append("2:").append(alphabet.get(totalPointsIndex));

        // Read the sheet
        List<LeagueStandingsRowRecord> leagueStandings = new ArrayList<>();
        for (LeagueStandingsRaceType leagueStandingsRaceType : leagueStandingsRaceTypes) {
            ValueRange valueRange = googleSheetsService.readSpreadsheetValuesInRange(
                    leagueStandingsSpreadSheetId, leagueStandingsRaceType.raceType() + range);
            List<List<Object>> standingsValues = valueRange.getValues();
            standingsValues.forEach(values -> leagueStandings.add(
                    new LeagueStandingsRowRecord(
                            leagueStandingsRaceType.raceType(),
                            String.valueOf(values.get(fullNameIndex - 1)),
                            String.valueOf(values.get(clubIndex - 1)),
                            Integer.parseInt(String.valueOf(values.get(totalPointsIndex - 1)))))
            );
        }

        // Ensure that the Standings are sorted on Total Points, and not accidentally on the Adjusted Total (Best X of Y).
        leagueStandings.sort(Comparator.comparing(LeagueStandingsRowRecord::raceCategory).reversed()
                .thenComparingInt(LeagueStandingsRowRecord::totalPoints).reversed());

        return leagueStandings;
    }

    public List<RiderGriddingPositionRecord> findSignups(List<BookingReportRowRecord> signups, int roundNumber) {
        return new ArrayList<>();
    }
}
