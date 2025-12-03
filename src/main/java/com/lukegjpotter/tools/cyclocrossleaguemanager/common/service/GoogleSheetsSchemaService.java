package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleSheetsSchemaService {

    private final RaceCategoryNameService raceCategoryNameService;
    private final LeagueStandingsHeader leagueStandingsHeader;
    private final BookingReportHeader bookingreportHeader;
    private final ResultsSheetHeader resultsSheetHeader;

    @Autowired
    public GoogleSheetsSchemaService(RaceCategoryNameService raceCategoryNameService) {
        this.raceCategoryNameService = raceCategoryNameService;
        leagueStandingsHeader = new LeagueStandingsHeader("Pos.", "Name", "Club", "Cat", "R", "Total", "Best");
        bookingreportHeader = new BookingReportHeader("TicketType", "First Name", "Last Name", "Gender", "CI Club");
        resultsSheetHeader = new ResultsSheetHeader("Pos.", "Name", "Club", "Category", "Gender");
    }

    public List<GriddingRaceType> griddingSchema() {
        return List.of(
                new GriddingRaceType(raceCategoryNameService.aRace(), "B4", 24),
                new GriddingRaceType(raceCategoryNameService.women(), "E4", 24),
                new GriddingRaceType(raceCategoryNameService.bRace(), "H4", 24),
                new GriddingRaceType(raceCategoryNameService.under16sMale(), "B31", 8),
                new GriddingRaceType(raceCategoryNameService.under16sFemale(), "E31", 8),
                new GriddingRaceType(raceCategoryNameService.under14sMale(), "B42", 8),
                new GriddingRaceType(raceCategoryNameService.under14sFemale(), "E42", 8),
                new GriddingRaceType(raceCategoryNameService.under12sMale(), "H31", 8),
                new GriddingRaceType(raceCategoryNameService.under12sFemale(), "H42", 8));
    }

    public List<LeagueStandingsRaceType> leagueStandingsSchema() {
        return List.of(
                new LeagueStandingsRaceType(raceCategoryNameService.aRace()),
                new LeagueStandingsRaceType(raceCategoryNameService.women()),
                new LeagueStandingsRaceType(raceCategoryNameService.bRace()),
                new LeagueStandingsRaceType(raceCategoryNameService.u16Male()),
                new LeagueStandingsRaceType(raceCategoryNameService.u16Female()),
                new LeagueStandingsRaceType(raceCategoryNameService.u14Male()),
                new LeagueStandingsRaceType(raceCategoryNameService.u14Female()),
                new LeagueStandingsRaceType(raceCategoryNameService.u12Male()),
                new LeagueStandingsRaceType(raceCategoryNameService.u12Female()));
    }

    public LeagueStandingsHeader leagueStandingsHeaders() {
        return leagueStandingsHeader;
    }

    public BookingReportHeader bookingReportHeaders() {
        return bookingreportHeader;
    }

    public ResultsSheetHeader resultsSheetHeaders() {
        return resultsSheetHeader;
    }
}

