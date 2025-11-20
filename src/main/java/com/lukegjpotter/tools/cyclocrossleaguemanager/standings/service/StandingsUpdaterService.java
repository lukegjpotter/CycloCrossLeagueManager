package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto.UpdateStandingsRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto.UpdateStandingsResponseRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.exception.UpdateStandingsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class StandingsUpdaterService {

    private static final Logger logger = LoggerFactory.getLogger(StandingsUpdaterService.class);
    @Value("${common.currentseason.standings}")
    private String currentSeasonLeagueStandingsSpreadSheetId;

    public UpdateStandingsResponseRecord updateStandings(final UpdateStandingsRequestRecord updateStandingsRequestRecord) {

        String roundResultsGoogleSheetId;

        // Ensure that the Results Spread Sheet is a correct URL.
        try {
            URL url = new URI(updateStandingsRequestRecord.roundResultsUrl()).toURL();
            url.openConnection().connect();
            roundResultsGoogleSheetId = url.getPath();
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

        return new UpdateStandingsResponseRecord(
                "https://docs.google.com/spreadsheets/d/"
                        + currentSeasonLeagueStandingsSpreadSheetId
                        + "/");
    }
}
