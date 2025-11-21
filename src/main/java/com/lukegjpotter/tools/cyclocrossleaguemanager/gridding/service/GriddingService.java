package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingResultRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.exception.GriddingException;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        logger.info("Gridding Sign Ups.");

        List<LeagueStandingsRowRecord> leagueStandings;
        List<BookingReportRowRecord> allSignups;

        // Ensure that the Gridding Sheet is a valid URL. Fail Fast Principle.
        try {
            URL griddingSheetURL = new URI(griddingRequestRecord.gridding()).toURL();
            griddingSheetURL.openConnection().connect();
        } catch (URISyntaxException | IOException exception) {
            throw new GriddingException("Error with Gridding Sheet URL.", exception);
        }

        // Get the Signups from the Booking Report. The Booking Report has to be changed from XLS to an actual Google Sheet.
        try {
            URL signupsSheetURL = new URI(griddingRequestRecord.signups()).toURL();
            signupsSheetURL.openConnection().connect();
            String signupsBookingReportGoogleSheetsId = signupsSheetURL.getPath().split("/")[3];
            allSignups = bookingReportRepository.getDataFromSignUpsGoogleSheet(signupsBookingReportGoogleSheetsId, true);
        } catch (URISyntaxException | IOException exception) {
            throw new GriddingException("Error when Loading Signups from Booking Report.", exception);
        }
        // Get the League Standings from the correct Standing Sheet. The sheets are properties in the application.properties.
        try {
            leagueStandings = leagueStandingsRepository.loadDataFromLeagueStandingsGoogleSheet(griddingRequestRecord.roundNumber());
        } catch (IOException ioException) {
            throw new GriddingException("Error when Loading League Standings.", ioException);
        }
        // Check if there are any Riders with UCI Points in the Sign-Ups, add them to RidersInGriddedOrder.
        // Remove ridersWithUciPoints from ridersSignedUp, so they are not double-counted.
        List<RiderGriddingPositionRecord> ridersInGriddedOrder = uciPointsRepository.findRidersWithUciPointsWhoAreSignedUp(allSignups);
        ridersInGriddedOrder.addAll(youthNationalChampionsRepository.findYouthNationalChampionsWhoAreSignedUp(allSignups));

        // Check the Standings position of each remaining ridersSignedUp, add them to RidersInGriddedOrder.
        ridersInGriddedOrder.addAll(leagueStandingsRepository.findLeaguePositionOfAllUngriddedSignups(
                leagueStandings, allSignups, ridersInGriddedOrder));

        return griddingRepository.writeGriddingToGoogleSheet(ridersInGriddedOrder, griddingRequestRecord.gridding());
    }
}
