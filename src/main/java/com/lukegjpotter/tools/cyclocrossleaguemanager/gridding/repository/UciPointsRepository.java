package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderUciPointRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UciPointsRepository {

    private final List<RiderUciPointRecord> ridersWithUciPoints;
    private final BookingReportRepository bookingReportRepository;


    public UciPointsRepository(BookingReportRepository bookingReportRepository) {
        this.bookingReportRepository = bookingReportRepository;
        ridersWithUciPoints = findAll();
    }

    public List<RiderUciPointRecord> findAll() {
        if (ridersWithUciPoints != null && !ridersWithUciPoints.isEmpty()) return ridersWithUciPoints;

        // ToDo: Use jSoup or Selenium to get the results.
        return new ArrayList<>();
    }

    public List<RiderGriddingPositionRecord> extractSignUps(List<BookingReportRowRecord> all) {

        // Delete Sign-ups with UCI Points, so they are not double counted.
        bookingReportRepository.deleteUciRiders(new ArrayList<BookingReportRowRecord>());

        return new ArrayList<>();
    }
}
