package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.component.TextUtilsComponent;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.RangeAndMinimumIndexRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsRangeBuilderService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.exception.UpdateStandingsException;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.LeagueStandingsAndRoundColumnRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.ResultRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.RiderNameAndCellRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.RiderNamePointsAndCellRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class LeagueStandingsWriteRepository {

    private final static Logger logger = LoggerFactory.getLogger(LeagueStandingsWriteRepository.class);
    private final GoogleSheetsService googleSheetsService;
    private final GoogleSheetsSchemaService googleSheetsSchemaService;
    private final GoogleSheetsRangeBuilderService googleSheetsRangeBuilderService;
    private final TextUtilsComponent textUtilsComponent;

    @Autowired
    public LeagueStandingsWriteRepository(GoogleSheetsService googleSheetsService, GoogleSheetsSchemaService googleSheetsSchemaService, GoogleSheetsRangeBuilderService googleSheetsRangeBuilderService, TextUtilsComponent textUtilsComponent) {
        this.googleSheetsService = googleSheetsService;
        this.googleSheetsSchemaService = googleSheetsSchemaService;
        this.googleSheetsRangeBuilderService = googleSheetsRangeBuilderService;
        this.textUtilsComponent = textUtilsComponent;
    }

    public void updateLeagueStandingsGoogleSheetWithRaceResults(final String currentSeasonLeagueStandingsSpreadSheetId, final int roundNumber, final HashMap<String, ResultRowRecord> resultRows) {
        // Fail Fast.
        if (currentSeasonLeagueStandingsSpreadSheetId.isBlank() || roundNumber < 1 || resultRows.isEmpty())
            throw new UpdateStandingsException("There is an issue with either the League Standings Spread Sheet ID, the Round Number, or the Results List.");

        logger.info("Updating League Standings with Race Results.");

        // Read League Standings.
        LeagueStandingsAndRoundColumnRecord leagueStandings = readLeagueStandingsAndCellsToWriteTo(currentSeasonLeagueStandingsSpreadSheetId, roundNumber);

        // Apply position points.
        List<RiderNamePointsAndCellRecord> riderNamePointsAndCells = applyPositionPointsToRiders(leagueStandings, resultRows);

        // Write League Standings.
        updateLeagueStandingsToGoogleSheet(currentSeasonLeagueStandingsSpreadSheetId, riderNamePointsAndCells);

        // ToDo: Sort League Standings Sheet.

        logger.info("Finished Updating League Standings.");
    }

    private LeagueStandingsAndRoundColumnRecord readLeagueStandingsAndCellsToWriteTo(final String currentSeasonLeagueStandingsSpreadSheetId, final int roundNumber) {
        logger.debug("Reading League Standings, to discover cells to write to.");
        // ToDo: Read League Standings.
        HashMap<String, RiderNameAndCellRecord> riderNameAndCells = new HashMap<>();
        HashMap<String, Character> roundColumnLetterHashMap = new HashMap<>();

        // Iterate through the sheets in the League Standings.
        googleSheetsSchemaService.leagueStandingsSchema().forEach(raceType -> {
            // Get the Spreadsheet Headers.
            List<String> spreadsheetHeaders;
            try {
                spreadsheetHeaders = googleSheetsService.getSpreadsheetHeaders(currentSeasonLeagueStandingsSpreadSheetId, raceType.raceType());
            } catch (IOException ioException) {
                throw new UpdateStandingsException("Error getting Spreadsheet Headers.", ioException);
            }
            // Set the Indices for the Important Columns.
            int positionIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().position());
            int fullNameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().fullName());
            int clubIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().club());
            int ageCategoryIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().ageCategory());
            int roundIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().roundPrefix() + roundNumber);
            // Build the Range.
            RangeAndMinimumIndexRecord rangeAndMinimumIndexRecord = googleSheetsRangeBuilderService.buildGoogleSheetsRange(raceType.raceType(), Stream.of(positionIndex, fullNameIndex, clubIndex, ageCategoryIndex, roundIndex));
            // Adjust the Indices to account for the minIndex.
            final int finalPositionIndex = positionIndex - rangeAndMinimumIndexRecord.minimumIndex();
            final int finalFullNameIndex = fullNameIndex - rangeAndMinimumIndexRecord.minimumIndex();
            final int finalClubIndex = clubIndex - rangeAndMinimumIndexRecord.minimumIndex();
            // ToDo: Is ageCategory being used in this method? Or is it useful in the returned object?
            final int finalAgeCategoryIndex = ageCategoryIndex - rangeAndMinimumIndexRecord.minimumIndex();
            final int finalRoundIndex = roundIndex - rangeAndMinimumIndexRecord.minimumIndex();
            final char roundColumnLetter = List.of('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R').get(finalRoundIndex);
            roundColumnLetterHashMap.put(raceType.raceType(), roundColumnLetter);

            // Read the League Standings Sheet.
            List<List<Object>> standingsValues;
            try {
                standingsValues = googleSheetsService.readSpreadsheetValuesInRange(currentSeasonLeagueStandingsSpreadSheetId, rangeAndMinimumIndexRecord.range()).getValues();
            } catch (IOException ioException) {
                throw new UpdateStandingsException("Error reading from League Standings Sheet", ioException);
            }
            standingsValues.forEach(leagueStandingsRow -> {
                RiderNameAndCellRecord riderInLeagueStandings = new RiderNameAndCellRecord(
                        raceType.raceType(),
                        textUtilsComponent.toIrishFormattedNameAndTitleCase(leagueStandingsRow.get(finalFullNameIndex).toString().trim()),
                        leagueStandingsRow.get(finalClubIndex).toString().trim(),
                        // FixMe: Using Position+1 relies on the sheet being in a Sorted State prior to reading it. So we'll need to sort the sheet.
                        raceType.raceType() + "!" + roundColumnLetter + (Integer.parseInt(leagueStandingsRow.get(finalPositionIndex).toString()) + 1));

                riderNameAndCells.put(
                        riderInLeagueStandings.raceCategory() + riderInLeagueStandings.riderFullName() + riderInLeagueStandings.club(),
                        riderInLeagueStandings);
            });
        });

        return new LeagueStandingsAndRoundColumnRecord(riderNameAndCells, roundColumnLetterHashMap);
    }

    private List<RiderNamePointsAndCellRecord> applyPositionPointsToRiders(final LeagueStandingsAndRoundColumnRecord leagueStandingsAndRoundColumn, final HashMap<String, ResultRowRecord> resultRowsHashMap) {
        logger.debug("Applying Points to Riders.");

        List<RiderNamePointsAndCellRecord> riderNamePointsAndCells = new ArrayList<>();
        HashMap<String, RiderNameAndCellRecord> leagueStandingsHashMap = leagueStandingsAndRoundColumn.leagueStandingsHashMap();
        HashMap<String, Integer> newRidersAddedPerRaceCategoryHashMap = new HashMap<>();

        resultRowsHashMap.forEach((key, resultRowRecord) -> {
            if (leagueStandingsHashMap.containsKey(key)) {
                RiderNameAndCellRecord leagueStandingRow = leagueStandingsHashMap.get(key);

                riderNamePointsAndCells.add(new RiderNamePointsAndCellRecord(
                        resultRowRecord.fullName(),
                        resultRowRecord.club(),
                        resultRowRecord.ageCategory(),
                        getPointsForPosition(resultRowRecord.position()),
                        leagueStandingRow.cellToUpdate(),
                        false));
            } else {
                char roundColumnLetter = leagueStandingsAndRoundColumn.roundColumnLetterHashMap().get(resultRowRecord.raceCategory());
                int numNewRidersAdded = newRidersAddedPerRaceCategoryHashMap.getOrDefault(resultRowRecord.raceCategory(), 1);
                newRidersAddedPerRaceCategoryHashMap.put(resultRowRecord.raceCategory(), numNewRidersAdded + 1);
                int numRiderInRaceCategory = leagueStandingsHashMap.keySet().stream()
                        .filter(leagueStandingsKey -> leagueStandingsKey.startsWith(resultRowRecord.raceCategory()))
                        .toList().size(); // FixMe: Potentially need a -1 here.

                int newRiderRow = numRiderInRaceCategory + numNewRidersAdded;

                riderNamePointsAndCells.add(new RiderNamePointsAndCellRecord(
                        resultRowRecord.fullName(),
                        resultRowRecord.club(),
                        resultRowRecord.ageCategory(),
                        getPointsForPosition(resultRowRecord.position()),
                        resultRowRecord.raceCategory() + "!" + roundColumnLetter + newRiderRow,
                        true));
            }
        });

        return riderNamePointsAndCells;
    }

    private int getPointsForPosition(final int position) {
        return switch (position) {
            case 1 -> 60;
            case 2 -> 55;
            case 3 -> 51;
            case 4 -> 48;
            case 5 -> 46;
            case 6 -> 45;
            case 7 -> 44;
            case 8 -> 43;
            case 9 -> 42;
            case 10 -> 41;
            case 11 -> 40;
            case 12 -> 39;
            case 13 -> 38;
            case 14 -> 37;
            case 15 -> 36;
            case 16 -> 35;
            case 17 -> 34;
            case 18 -> 33;
            case 19 -> 32;
            case 20 -> 31;
            case 21 -> 30;
            case 22 -> 29;
            case 23 -> 28;
            case 24 -> 27;
            case 25 -> 26;
            case 26 -> 25;
            case 27 -> 24;
            case 28 -> 23;
            case 29 -> 22;
            case 30 -> 21;
            case 31 -> 20;
            case 32 -> 19;
            case 33 -> 18;
            case 34 -> 17;
            case 35 -> 16;
            case 36 -> 15;
            case 37 -> 14;
            case 38 -> 13;
            case 39 -> 12;
            case 40 -> 11;
            case 41 -> 10;
            case 42 -> 9;
            case 43 -> 8;
            case 44 -> 7;
            case 45 -> 6;
            case 46 -> 5;
            case 47 -> 4;
            case 48 -> 3;
            case 49 -> 2;
            default -> 1;
        };
    }

    private void updateLeagueStandingsToGoogleSheet(final String currentSeasonLeagueStandingsSpreadSheetId, final List<RiderNamePointsAndCellRecord> riderNamePointsAndCells) {
        logger.debug("Writing Standings to Google Sheet.");

        riderNamePointsAndCells.forEach(riderNamePointsAndCell -> {
            try {
                if (riderNamePointsAndCell.requiresNewRiderEntry()) {
                    String sheetName = riderNamePointsAndCell.cellToUpdate().split("!")[0];

                    List<String> spreadsheetHeaders = googleSheetsService.getSpreadsheetHeaders(currentSeasonLeagueStandingsSpreadSheetId, sheetName);
                    int fullNameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.leagueStandingsHeaders().fullName());
                    RangeAndMinimumIndexRecord rangeAndMinimumIndexRecord = googleSheetsRangeBuilderService.buildGoogleSheetsRange(sheetName, Stream.of(fullNameIndex));
                    String column = String.valueOf(rangeAndMinimumIndexRecord.range().split("!")[1].charAt(0));

                    int rowNumber = Integer.parseInt(riderNamePointsAndCell.cellToUpdate().replaceAll("\\D+", ""));

                    // Write Name, Club and Age Category to the SheetName!ColumnRowNumber
                    googleSheetsService.writeValuesToSpreadsheetFromCell(currentSeasonLeagueStandingsSpreadSheetId, sheetName + "!" + column + rowNumber, new ValueRange().setValues(List.of(List.of(riderNamePointsAndCell.riderFullName(), riderNamePointsAndCell.club(), riderNamePointsAndCell.ageCategory()))));
                }
                // Write the Points to the Points Column.
                googleSheetsService.writeValuesToSpreadsheetFromCell(currentSeasonLeagueStandingsSpreadSheetId, riderNamePointsAndCell.cellToUpdate(), new ValueRange().setValues(List.of(List.of(riderNamePointsAndCell.points()))));

            } catch (IOException ioException) {
                throw new UpdateStandingsException("Error when updating standings for: " + riderNamePointsAndCell, ioException);
            }
        });
    }
}
