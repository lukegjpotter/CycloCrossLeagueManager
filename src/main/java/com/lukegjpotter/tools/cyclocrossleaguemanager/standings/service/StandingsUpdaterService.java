package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto.UpdateStandingsRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto.UpdateStandingsResponseRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.exception.UpdateStandingsException;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.ResultRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.repository.LeagueStandingsWriteRepository;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.repository.ResultsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

@Service
public class StandingsUpdaterService {

    private static final Logger logger = LoggerFactory.getLogger(StandingsUpdaterService.class);
    private final ResultsRepository resultsRepository;
    private final LeagueStandingsWriteRepository leagueStandingsWriteRepository;
    @Value("${common.currentseason.standings}")
    private String currentSeasonLeagueStandingsSpreadSheetId;

    @Autowired
    public StandingsUpdaterService(ResultsRepository resultsRepository, LeagueStandingsWriteRepository leagueStandingsWriteRepository) {
        this.resultsRepository = resultsRepository;
        this.leagueStandingsWriteRepository = leagueStandingsWriteRepository;
    }

    public UpdateStandingsResponseRecord updateStandings(final UpdateStandingsRequestRecord updateStandingsRequestRecord) {

        String roundResultsGoogleSheetId;

        // Ensure that the Results Spread Sheet is a correct URL.
        try {
            URL url = new URI(updateStandingsRequestRecord.roundResultsUrl()).toURL();
            url.openConnection().connect();
            roundResultsGoogleSheetId = url.getPath().split("/")[3];
            assert (!roundResultsGoogleSheetId.isEmpty());
        } catch (URISyntaxException | IOException | AssertionError exception) {
            throw new UpdateStandingsException("Results Google Sheet is not a valid URL.", exception);
        }

        // Ensure that the League Standings sheet is a correct URL.
        // ToDo: This check should be at start-up, as it is a property.
        try {
            new URI("https://docs.google.com/spreadsheets/d/"
                    + currentSeasonLeagueStandingsSpreadSheetId
                    + "/").toURL().openConnection().connect();
        } catch (URISyntaxException | IOException exception) {
            throw new UpdateStandingsException("League Standings Google Sheet is not a valid URL.", exception);
        }

        // Get the Race Results.
        HashMap<String, ResultRowRecord> resultRows = resultsRepository.getResultRowsFromResultsGoogleSheet(roundResultsGoogleSheetId);

        // Write Standings updates to League Standings Google Sheet.
        try {
            leagueStandingsWriteRepository.updateLeagueStandingsGoogleSheetWithRaceResults(currentSeasonLeagueStandingsSpreadSheetId, updateStandingsRequestRecord.roundNumber(), resultRows);
        } catch (IOException ioException) {
            throw new UpdateStandingsException("Error Updating the League Standings Sheet.", ioException);
        }

        return new UpdateStandingsResponseRecord(
                "https://docs.google.com/spreadsheets/d/"
                        + currentSeasonLeagueStandingsSpreadSheetId
                        + "/");
    }
}
