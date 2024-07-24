package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingResultRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.BookingReportRepository;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.GriddingRepository;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.LeagueStandingsRepository;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.UciPointsRepository;
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

    public GriddingService(LeagueStandingsRepository leagueStandingsRepository, UciPointsRepository uciPointsRepository, BookingReportRepository bookingReportRepository, GriddingRepository griddingRepository) {
        this.leagueStandingsRepository = leagueStandingsRepository;
        this.uciPointsRepository = uciPointsRepository;
        this.bookingReportRepository = bookingReportRepository;
        this.griddingRepository = griddingRepository;
    }

    public GriddingResultRecord gridSignups(GriddingRequestRecord griddingRequestRecord) {

        bookingReportRepository.loadDataFromSignUpsGoogleSheet(griddingRequestRecord.signups());
        griddingRepository.setOutputGriddingGoogleSheet(griddingRequestRecord.gridding());
        // Check if there are any Riders with UCI Points in the Sign-Ups, add them to RidersInGriddedOrder.
        // Remove ridersWithUciPoints from ridersSignedUp, so they are not double counted.
        List<RiderGriddingPositionRecord> ridersInGriddedOrder = uciPointsRepository.extractSignUps(bookingReportRepository.findAll());

        // Check the Standings position of each remaining ridersSignedUp, add them to RidersInGriddedOrder.
        ridersInGriddedOrder.addAll(leagueStandingsRepository.findSignups(bookingReportRepository.findAll(), griddingRequestRecord.roundNumber()));

        return griddingRepository.saveAll(ridersInGriddedOrder);
    }
}
