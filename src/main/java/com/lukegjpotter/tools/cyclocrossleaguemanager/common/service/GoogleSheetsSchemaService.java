package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.BookingReportHeader;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.GriddingRaceType;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.LeagueStandingsHeader;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.LeagueStandingsRaceType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleSheetsSchemaService {

    private final LeagueStandingsHeader leagueStandingsHeader;
    private final BookingReportHeader bookingreportHeader;

    public GoogleSheetsSchemaService() {
        leagueStandingsHeader = new LeagueStandingsHeader("Name", "Club", "Total");
        bookingreportHeader = new BookingReportHeader("TicketType", "First Name", "Last Name", "Gender", "CI Club", "Team");
    }

    public List<GriddingRaceType> griddingSchema() {
        return List.of(
                new GriddingRaceType("A-Race", "B4", 24),
                new GriddingRaceType("Women", "E4", 24),
                new GriddingRaceType("B-Race", "H4", 24),
                new GriddingRaceType("Under 16s Male", "B31", 8),
                new GriddingRaceType("Under 16s Female", "E31", 8),
                new GriddingRaceType("Under 14s Male", "B42", 8),
                new GriddingRaceType("Under 14s Female", "E42", 8),
                new GriddingRaceType("Under 12s Male", "H31", 8),
                new GriddingRaceType("Under 12s Female", "H42", 8));
    }

    public List<LeagueStandingsRaceType> leagueStandingsSchema() {
        return List.of(
                new LeagueStandingsRaceType("A-Race"),
                new LeagueStandingsRaceType("Women"),
                new LeagueStandingsRaceType("B-Race"),
                new LeagueStandingsRaceType("U16 Male"),
                new LeagueStandingsRaceType("U16 Female"),
                new LeagueStandingsRaceType("U14 Male"),
                new LeagueStandingsRaceType("U14 Female"),
                new LeagueStandingsRaceType("U12 Male"),
                new LeagueStandingsRaceType("U12 Female"));
    }

    public LeagueStandingsHeader leagueStandingsHeaders() {
        return leagueStandingsHeader;
    }

    public BookingReportHeader bookingReportHeaders() {
        return bookingreportHeader;
    }
}

