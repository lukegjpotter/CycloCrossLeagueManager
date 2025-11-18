package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto.UpdateStandingsRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto.UpdateStandingsResponseRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.exception.UpdateStandingsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StandingsUpdaterService {

    private static final Logger logger = LoggerFactory.getLogger(StandingsUpdaterService.class);
    @Value("${common.currentseason.standings}")
    private String currentSeasonLeagueStandingsSpreadSheetId;

    public UpdateStandingsResponseRecord updateStandings(final UpdateStandingsRequestRecord updateStandingsRequestRecord) {

        if (updateStandingsRequestRecord.roundNumber() == 69)
            throw new UpdateStandingsException("Error when updating Standings.");

        return new UpdateStandingsResponseRecord(
                "https://docs.google.com/spreadsheets/d/"
                        + currentSeasonLeagueStandingsSpreadSheetId
                        + "/");
    }
}
