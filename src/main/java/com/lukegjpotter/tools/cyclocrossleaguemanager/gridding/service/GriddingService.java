package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingResultRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
public class GriddingService {

    private static final Logger logger = LoggerFactory.getLogger(GriddingService.class);
    private final LeagueStandingsRepository leagueStandingsRepository;
    private final UciPointsRepository uciPointsRepository;
    private final BookingReportRepository bookingReportRepository;
    private final GriddingRepository griddingRepository;
    private final YouthNationalChampionsRepository youthNationalChampionsRepository;

    public GriddingService(LeagueStandingsRepository leagueStandingsRepository, UciPointsRepository uciPointsRepository, BookingReportRepository bookingReportRepository, GriddingRepository griddingRepository, YouthNationalChampionsRepository youthNationalChampionsRepository) {
        this.leagueStandingsRepository = leagueStandingsRepository;
        this.uciPointsRepository = uciPointsRepository;
        this.bookingReportRepository = bookingReportRepository;
        this.griddingRepository = griddingRepository;
        this.youthNationalChampionsRepository = youthNationalChampionsRepository;
    }

    public GriddingResultRecord gridSignups(GriddingRequestRecord griddingRequestRecord) {
        logger.trace("Gridding Sign Ups.");

        List<LeagueStandingsRowRecord> leagueStandings;
        List<BookingReportRowRecord> allSignups;

        try {
            String signupsBookingReportGoogleSheetsId = new URL(griddingRequestRecord.signups()).getPath().split("/")[3];
            allSignups = bookingReportRepository.getDataFromSignUpsGoogleSheet(signupsBookingReportGoogleSheetsId);
        } catch (IOException e) {
            String errorMessage = "Error when Loading Signups from Booking Report,";
            logger.error("{} Error: {}", errorMessage, e.getMessage());
            return new GriddingResultRecord(griddingRequestRecord.gridding(), errorMessage + " Error: " + e.getMessage());
        }
        try {
            leagueStandings = leagueStandingsRepository.loadDataFromLeagueStandingsGoogleSheet(griddingRequestRecord.roundNumber());
        } catch (IOException e) {
            String errorMessage = "Error when Loading League Standings.";
            logger.error("{} Error: {}", errorMessage, e.getMessage());
            return new GriddingResultRecord(griddingRequestRecord.gridding(), errorMessage + " Error: " + e.getMessage());
        }
        // Check if there are any Riders with UCI Points in the Sign-Ups, add them to RidersInGriddedOrder.
        // Remove ridersWithUciPoints from ridersSignedUp, so they are not double counted.
        List<RiderGriddingPositionRecord> ridersInGriddedOrder = uciPointsRepository.findRidersWithUciPointsWhoAreSignedUp(allSignups);
        ridersInGriddedOrder.addAll(youthNationalChampionsRepository.findYouthNationalChampionsWhoAreSignedUp(allSignups));

        // Check the Standings position of each remaining ridersSignedUp, add them to RidersInGriddedOrder.
        ridersInGriddedOrder.addAll(leagueStandingsRepository.findLeaguePositionOfAllUngriddedSignups(
                leagueStandings, allSignups, ridersInGriddedOrder, griddingRequestRecord.roundNumber()));

        return griddingRepository.writeGriddingToGoogleSheet(ridersInGriddedOrder, griddingRequestRecord.gridding());
    }
}
