package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LeagueStandingsRepository {

    private final List<LeagueStandingsRowRecord> leagueStandings;

    public LeagueStandingsRepository() {
        leagueStandings = findAll();
    }

    public List<LeagueStandingsRowRecord> findAll() {
        if (leagueStandings != null && !leagueStandings.isEmpty()) return leagueStandings;

        // ToDo: Load Standings from Google Sheet.
        return new ArrayList<>();
    }

    public List<RiderGriddingPositionRecord> findSignups(List<BookingReportRowRecord> signups, int roundNumber) {
        return new ArrayList<>();
    }
}
