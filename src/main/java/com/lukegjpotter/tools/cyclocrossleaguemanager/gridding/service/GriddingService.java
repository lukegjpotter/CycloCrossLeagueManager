package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingResultRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

        bookingReportRepository.loadDataFromSignUpsGoogleSheet(griddingRequestRecord.signups());
        // Check if there are any Riders with UCI Points in the Sign-Ups, add them to RidersInGriddedOrder.
        // Remove ridersWithUciPoints from ridersSignedUp, so they are not double counted.
        List<RiderGriddingPositionRecord> ridersInGriddedOrder = uciPointsRepository.extractSignUps(bookingReportRepository.findAll());
        ridersInGriddedOrder.addAll(youthNationalChampionsRepository.findAll(bookingReportRepository.findAll()));

        // Check the Standings position of each remaining ridersSignedUp, add them to RidersInGriddedOrder.
        ridersInGriddedOrder.addAll(leagueStandingsRepository.findSignups(bookingReportRepository.findAll(), griddingRequestRecord.roundNumber()));

        return griddingRepository.writeGriddingToGoogleSheet(ridersInGriddedOrder, griddingRequestRecord.gridding());
    }
}
