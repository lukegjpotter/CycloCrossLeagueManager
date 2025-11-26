package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.LeagueStandingsRaceType;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.exception.GriddingException;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class LeagueStandingsRepository {

    private static final Logger logger = LoggerFactory.getLogger(LeagueStandingsRepository.class);
    private final GoogleSheetsService googleSheetsService;
    private final GoogleSheetsSchemaService googleSheetsSchemaService;
    @Value("${common.currentseason.standings}")
    private String currentSeasonLeagueStandingsSpreadSheetId;
    @Value("${gridding.lastseason.standings}")
    private String lastSeasonLeagueStandingsSpreadSheetId;

    @Autowired
    public LeagueStandingsRepository(GoogleSheetsService googleSheetsService, GoogleSheetsSchemaService googleSheetsSchemaService) {
        this.googleSheetsService = googleSheetsService;
        this.googleSheetsSchemaService = googleSheetsSchemaService;
    }

    public List<LeagueStandingsRowRecord> loadDataFromLeagueStandingsGoogleSheet(final int roundNumber) throws IOException {

        logger.info("Loading data from League Standings Google Sheet.");
        // Decide which year's League Standings to use.
        String leagueStandingsSpreadSheetId = getLeagueStandingsSpreadSheetId(roundNumber);

        // Ensure that the League Standings actually exists.
        try {
            new URI("https://docs.google.com/spreadsheets/d/" + leagueStandingsSpreadSheetId + "/").toURL().openConnection().connect();
        } catch (URISyntaxException | IOException exception) {
            throw new GriddingException("Error with League Standings Sheet.", exception);
        }

        // Set the Indices of the important columns.
        List<LeagueStandingsRaceType> leagueStandingsRaceTypes = googleSheetsSchemaService.leagueStandingsSchema();
        List<String> spreadsheetHeaders = googleSheetsService.getSpreadsheetHeaders(
                leagueStandingsSpreadSheetId, leagueStandingsRaceTypes.get(0).raceType());
        int fullNameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().fullName());
        int clubIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().club());
        int totalPointsIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().totalPoints());

        int minIndex = Stream.of(fullNameIndex, clubIndex, totalPointsIndex).min(Integer::compareTo).get();
        int maxIndex = Stream.of(fullNameIndex, clubIndex, totalPointsIndex).max(Integer::compareTo).get();

        // Build the Range
        // ToDo: Extract this functionality to a Helper Method.
        StringBuilder range = new StringBuilder("!");
        List<String> alphabet = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        range.append(alphabet.get(minIndex)).append("2:").append(alphabet.get(maxIndex));

        final int finalFullNameIndex = fullNameIndex - minIndex;
        final int finalClubIndex = clubIndex - minIndex;
        final int finalTotalPointsIndex = totalPointsIndex - minIndex;

        // Read the sheet
        List<LeagueStandingsRowRecord> leagueStandings = new ArrayList<>();
        for (LeagueStandingsRaceType leagueStandingsRaceType : leagueStandingsRaceTypes) {
            ValueRange valueRange = googleSheetsService.readSpreadsheetValuesInRange(
                    leagueStandingsSpreadSheetId, leagueStandingsRaceType.raceType() + range);
            List<List<Object>> standingsValues = valueRange.getValues();

            // Map the league race type to the gridding output race type.
            String raceType = leagueStandingsRaceType.raceType();
            int raceTypeIndex = leagueStandingsRaceTypes.indexOf(new LeagueStandingsRaceType(raceType));
            String raceCategory = googleSheetsSchemaService.griddingSchema().get(raceTypeIndex).raceCategory();

            standingsValues.forEach(values -> leagueStandings.add(
                    new LeagueStandingsRowRecord(
                            raceCategory,
                            String.valueOf(values.get(finalFullNameIndex)),
                            String.valueOf(values.get(finalClubIndex)),
                            Integer.parseInt(String.valueOf(values.get(finalTotalPointsIndex)))
                    )));
        }

        // Ensure that the Standings are sorted on Total Points, and not accidentally on the Adjusted Total (Best X of Y).
        leagueStandings.sort(Comparator.comparing(LeagueStandingsRowRecord::raceCategory).reversed()
                .thenComparingInt(LeagueStandingsRowRecord::totalPoints).reversed());

        logger.trace("League Standings Size: {}.", leagueStandings.size());

        return leagueStandings;
    }

    private String getLeagueStandingsSpreadSheetId(final int roundNumber) {
        String leagueStandingsSpreadSheetId = (roundNumber == 1) ?
                lastSeasonLeagueStandingsSpreadSheetId : currentSeasonLeagueStandingsSpreadSheetId;

        // If it's the first season of a league the previous season standings may be an empty property. In this case,
        // there is no need to perform gridding.
        try {
            assert (!leagueStandingsSpreadSheetId.isEmpty());
        } catch (AssertionError assertionError) {
            throw new GriddingException("League Standing SpreadSheet ID is blank.", assertionError);
        }
        return leagueStandingsSpreadSheetId;
    }

    public List<RiderGriddingPositionRecord> findLeaguePositionOfAllUngriddedSignups(
            final List<LeagueStandingsRowRecord> leagueStandings, final List<BookingReportRowRecord> signups, final List<RiderGriddingPositionRecord> alreadyGriddedRidersInOrder) {

        logger.info("Finding League Position of all Ungridded Signups.");

        List<RiderGriddingPositionRecord> ridersInGriddedOrder = new ArrayList<>();
        // LatestGriddingOrder is needed because the Second time of adding a rider in a race category, needs to get the
        // updated grid positions from ridersInGriddedOrder.
        List<RiderGriddingPositionRecord> latestGriddingOrder = new ArrayList<>(alreadyGriddedRidersInOrder);

        /* Iterate through League Standings.
         * If an entry in league standings is in the signups.
         * Add the rider to ridersInGriddedOrder in the appropriate position. */
        for (LeagueStandingsRowRecord riderInLeagueStandings : leagueStandings) {

            boolean isSignedUpRiderInLeague = false;
            for (BookingReportRowRecord signup : signups) {
                if (riderInLeagueStandings.raceCategory().equalsIgnoreCase(signup.raceCategory())
                        && riderInLeagueStandings.fullName().equalsIgnoreCase(signup.fullName())
                        && riderInLeagueStandings.club().equalsIgnoreCase(signup.clubName())) {
                    isSignedUpRiderInLeague = true;
                    break;
                }
            }

            if (isSignedUpRiderInLeague) {
                // Is the rider already gridded? If yes, then break this loop.
                boolean isRiderAlreadyGridded = false;
                for (RiderGriddingPositionRecord griddedRider : latestGriddingOrder) {
                    if (griddedRider.raceCategory().startsWith(riderInLeagueStandings.raceCategory())
                            && griddedRider.fullName().equalsIgnoreCase(riderInLeagueStandings.fullName())
                            && griddedRider.clubName().equalsIgnoreCase(riderInLeagueStandings.club())) {
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

                    RiderGriddingPositionRecord newRiderToGrid = new RiderGriddingPositionRecord(riderInLeagueStandings.raceCategory(), gridPosition, riderInLeagueStandings.fullName(), riderInLeagueStandings.club());
                    ridersInGriddedOrder.add(newRiderToGrid);
                    latestGriddingOrder.add(newRiderToGrid);
                }
            }
        }

        int maxIndexToSublistForLogMessage = (ridersInGriddedOrder.size() >= 5) ? 5 : (ridersInGriddedOrder.isEmpty()) ? 0 : ridersInGriddedOrder.size();
        logger.trace("Gridded riders based solely on League Position includes: {}.", ridersInGriddedOrder.subList(0, maxIndexToSublistForLogMessage));

        return ridersInGriddedOrder;
    }
}
