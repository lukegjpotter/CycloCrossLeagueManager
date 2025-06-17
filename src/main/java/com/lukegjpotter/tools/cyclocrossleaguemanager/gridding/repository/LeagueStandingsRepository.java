package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.LeagueStandingsRaceType;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class LeagueStandingsRepository {

    private static final Logger logger = LoggerFactory.getLogger(LeagueStandingsRepository.class);
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

        logger.trace("Loading data from League Standings Google Sheet.");
        // Decide which year's League Standings to use.
        String leagueStandingsSpreadSheetId = (roundNumber == 1) ?
                lastSeasonLeagueStandingsSpreadSheetId : currentSeasonLeagueStandingsSpreadSheetId;

        // Set the Indices of the important columns.
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
                            Integer.parseInt(String.valueOf(values.get(totalPointsIndex - 1))))));
        }

        // Ensure that the Standings are sorted on Total Points, and not accidentally on the Adjusted Total (Best X of Y).
        leagueStandings.sort(Comparator.comparing(LeagueStandingsRowRecord::raceCategory).reversed()
                .thenComparingInt(LeagueStandingsRowRecord::totalPoints).reversed());

        logger.trace("League Standings Size: {}.", leagueStandings.size());

        return leagueStandings;
    }

    public List<RiderGriddingPositionRecord> findLeaguePositionOfAllUngriddedSignups(
            final List<LeagueStandingsRowRecord> leagueStandings, final List<BookingReportRowRecord> signups, final List<RiderGriddingPositionRecord> alreadyGriddedRidersInOrder) {

        logger.trace("Finding League Position of all Ungridded Signups.");

        List<RiderGriddingPositionRecord> ridersInGriddedOrder = new ArrayList<>();
        // LatestGriddingOrder is needed because the Second time of adding a rider in a race category, needs to get the updated grid positions from ridersInGriddedOrder.
        List<RiderGriddingPositionRecord> latestGriddingOrder = new ArrayList<>(alreadyGriddedRidersInOrder);

        // Iterate through League Standings
        // If an entry in league standings is in the signups.
        // Add the rider to ridersInGriddedOrder in the appropriate position.
        for (LeagueStandingsRowRecord riderInLeagueStandings : leagueStandings) {
            String raceCategoryWithoutSuffix = (riderInLeagueStandings.raceCategory().startsWith("Under")) ? riderInLeagueStandings.raceCategory() : riderInLeagueStandings.raceCategory().split(" ")[0];

            boolean isSignedUpRiderInLeague = false;
            for (BookingReportRowRecord signup : signups) {
                if (riderInLeagueStandings.raceCategory().equals(raceCategoryWithoutSuffix)
                        && riderInLeagueStandings.fullName().equals(signup.fullName())
                        && riderInLeagueStandings.Club().equals(signup.clubName())) {
                    isSignedUpRiderInLeague = true;
                    break;
                }
            }

            if (isSignedUpRiderInLeague) {
                // Is the rider already gridded? If yes, then break this loop.
                boolean isRiderAlreadyGridded = false;
                for (RiderGriddingPositionRecord griddedRider : latestGriddingOrder) {
                    if (griddedRider.raceCategory().startsWith(riderInLeagueStandings.raceCategory())
                            && griddedRider.fullName().equals(riderInLeagueStandings.fullName())
                            && griddedRider.clubName().equals(riderInLeagueStandings.Club())) {
                        isRiderAlreadyGridded = true;
                        break;
                    }
                }

                if (!isRiderAlreadyGridded) {

                    List<RiderGriddingPositionRecord> ridersInGriddedOrderForRaceCategory = latestGriddingOrder.stream()
                            .filter(riderGriddingPositionRecord -> riderGriddingPositionRecord.raceCategory().startsWith(riderInLeagueStandings.raceCategory()))
                            .sorted(Comparator.comparingInt(RiderGriddingPositionRecord::gridPosition).reversed())
                            .toList();

                    int gridPosition = (ridersInGriddedOrderForRaceCategory.isEmpty()) ? 1 : ridersInGriddedOrderForRaceCategory.get(0).gridPosition() + 1;

                    RiderGriddingPositionRecord newRiderToGrid = new RiderGriddingPositionRecord(riderInLeagueStandings.raceCategory(), gridPosition, riderInLeagueStandings.fullName(), riderInLeagueStandings.Club());
                    ridersInGriddedOrder.add(newRiderToGrid);
                    latestGriddingOrder.add(newRiderToGrid);
                }
            }
        }

        int maxIndexToSublistForLogMessage = (ridersInGriddedOrder.size() >= 5) ? 5 : (ridersInGriddedOrder.isEmpty()) ? 0 : ridersInGriddedOrder.size() - 1;
        logger.trace("Gridded rider based solely on League Position includes: {}.", ridersInGriddedOrder.subList(0, maxIndexToSublistForLogMessage));
        return ridersInGriddedOrder;
    }
}
