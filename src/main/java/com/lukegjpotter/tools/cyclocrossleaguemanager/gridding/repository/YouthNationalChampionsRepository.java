package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class YouthNationalChampionsRepository {

    // ToDo: Webservice call to get the U16 and U14 National Champions.

    public Collection<RiderGriddingPositionRecord> findAll(List<BookingReportRowRecord> all) {
        return new ArrayList<>();
    }
}
