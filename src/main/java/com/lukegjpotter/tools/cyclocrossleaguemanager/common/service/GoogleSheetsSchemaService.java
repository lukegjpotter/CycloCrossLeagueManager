package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.BookingReportHeader;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.GriddingRaceType;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.LeagueStandingsHeader;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.LeagueStandingsRaceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleSheetsSchemaService {

    @Autowired
    private RaceCategoryNameService raceCategoryNameService;
    private final LeagueStandingsHeader leagueStandingsHeader;
    private final BookingReportHeader bookingreportHeader;

    public GoogleSheetsSchemaService() {
        leagueStandingsHeader = new LeagueStandingsHeader("Name", "Club", "Total");
        bookingreportHeader = new BookingReportHeader("TicketType", "First Name", "Last Name", "Gender", "CI Club", "Team");
    }

    public List<GriddingRaceType> griddingSchema() {
        return List.of(
                new GriddingRaceType(raceCategoryNameService.aRace(), "B4", 24),
                new GriddingRaceType(raceCategoryNameService.women(), "E4", 24),
                new GriddingRaceType(raceCategoryNameService.bRace(), "H4", 24),
                new GriddingRaceType(raceCategoryNameService.u16Male(), "B31", 8),
                new GriddingRaceType(raceCategoryNameService.u16Female(), "E31", 8),
                new GriddingRaceType(raceCategoryNameService.u14Male(), "B42", 8),
                new GriddingRaceType(raceCategoryNameService.u14Female(), "E42", 8),
                new GriddingRaceType(raceCategoryNameService.u12Male(), "H31", 8),
                new GriddingRaceType(raceCategoryNameService.u12Female(), "H42", 8));
    }

    public List<LeagueStandingsRaceType> leagueStandingsSchema() {
        return List.of(
                new LeagueStandingsRaceType(raceCategoryNameService.aRace()),
                new LeagueStandingsRaceType(raceCategoryNameService.women()),
                new LeagueStandingsRaceType(raceCategoryNameService.bRace()),
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

