package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.component.TextUtilsComponent;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.RangeAndMinimumIndexRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsRangeBuilderService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsSchemaService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.RaceCategoryNameService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.exception.UpdateStandingsException;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.ResultRowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class ResultsRepository {

    private static final Logger logger = LoggerFactory.getLogger(ResultsRepository.class);
    private final GoogleSheetsService googleSheetsService;
    private final GoogleSheetsSchemaService googleSheetsSchemaService;
    private final RaceCategoryNameService raceCategoryNameService;
    private final TextUtilsComponent textUtilsComponent;
    private final GoogleSheetsRangeBuilderService googleSheetsRangeBuilderService;

    @Autowired
    public ResultsRepository(GoogleSheetsService googleSheetsService, GoogleSheetsSchemaService googleSheetsSchemaService, RaceCategoryNameService raceCategoryNameService, TextUtilsComponent textUtilsComponent, GoogleSheetsRangeBuilderService googleSheetsRangeBuilderService) {
        this.googleSheetsService = googleSheetsService;
        this.googleSheetsSchemaService = googleSheetsSchemaService;
        this.raceCategoryNameService = raceCategoryNameService;
        this.textUtilsComponent = textUtilsComponent;
        this.googleSheetsRangeBuilderService = googleSheetsRangeBuilderService;
    }

    public List<ResultRowRecord> getResultRowsFromResultsGoogleSheet(final String roundResultsGoogleSheetId) {

        logger.info("Getting Data from Results Google Sheet.");
        List<ResultRowRecord> resultRows = new ArrayList<>();

        List.of(raceCategoryNameService.aRace(),
                raceCategoryNameService.bRace(),
                raceCategoryNameService.women(),
                raceCategoryNameService.youth(),
                raceCategoryNameService.underage()
        ).forEach(sheetNameRaceCategory -> {

            // Set the Indices of the important columns.
            List<String> spreadsheetHeaders = null;
            try {
                spreadsheetHeaders = googleSheetsService.getSpreadsheetHeaders(roundResultsGoogleSheetId, sheetNameRaceCategory);
            } catch (IOException ioException) {
                throw new UpdateStandingsException("Error reading headers from Results Sheet", ioException);
            }
            int positionIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.resultsSheetHeaders().position());
            int fullNameIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.resultsSheetHeaders().fullName());
            int clubIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.resultsSheetHeaders().club());
            int ageCategoryIndex = spreadsheetHeaders.indexOf(googleSheetsSchemaService.resultsSheetHeaders().ageCategory());

            // Build the Range.
            RangeAndMinimumIndexRecord rangeAndMinimumIndexRecord = googleSheetsRangeBuilderService.buildGoogleSheetsRange(sheetNameRaceCategory, Stream.of(positionIndex, fullNameIndex, ageCategoryIndex, clubIndex));

            // Adjust the Indices to account for the minIndex.
            final int finalPositionIndex = positionIndex - rangeAndMinimumIndexRecord.minimumIndex();
            final int finalFullNameIndex = fullNameIndex - rangeAndMinimumIndexRecord.minimumIndex();
            final int finalClubIndex = clubIndex - rangeAndMinimumIndexRecord.minimumIndex();
            final int finalAgeCategoryIndex = ageCategoryIndex - rangeAndMinimumIndexRecord.minimumIndex();

            // Read the Results Sheet.
            ValueRange valueRange = null;
            try {
                valueRange = googleSheetsService.readSpreadsheetValuesInRange(roundResultsGoogleSheetId, rangeAndMinimumIndexRecord.range());
            } catch (IOException ioException) {
                throw new UpdateStandingsException("Error reading range from Results Sheet", ioException);
            }

            List<List<Object>> resultRowValues = valueRange.getValues().stream().filter(row -> row.size() >= 4).toList();
            resultRowValues.forEach(values -> {
                int position = Integer.parseInt(values.get(finalPositionIndex).toString().trim().replace(".", ""));
                String fullName = textUtilsComponent.toIrishFormattedNameAndTitleCase(
                        String.valueOf(values.get(finalFullNameIndex)).trim());

                String club = String.valueOf(values.get(finalClubIndex)).trim();
                String ageCategory = textUtilsComponent.toTitleCase(String.valueOf(values.get(finalAgeCategoryIndex)).trim());
                // Gender is not always on every sheet.
                String gender;
                try {
                    gender = ageCategory.split(" ")[2];
                } catch (IndexOutOfBoundsException ignored) {
                    gender = "";
                }

                ResultRowRecord resultRow = new ResultRowRecord(sheetNameRaceCategory, position, fullName, club, ageCategory, gender);
                logger.trace("Results Sheet Row: {}", resultRow);
                resultRows.add(resultRow);
            });
        });

        resultRows.sort(Comparator
                .comparing(ResultRowRecord::raceCategory)
                .thenComparingInt(ResultRowRecord::position)
                .thenComparing(ResultRowRecord::ageCategory));

        return resultRows;
    }
}
