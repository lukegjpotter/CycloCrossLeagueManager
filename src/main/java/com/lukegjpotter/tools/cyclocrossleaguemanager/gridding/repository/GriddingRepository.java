package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingResultRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GriddingRepository {

    private String gridding;

    public GriddingResultRecord saveAll(List<RiderGriddingPositionRecord> ridersInGriddedOrder) {

        // ToDo: Write ridersInGriddedOrder to Google Sheet.

        return new GriddingResultRecord(gridding, "");
    }

    public void setOutputGriddingGoogleSheet(String gridding) {
        this.gridding = gridding;
    }
}
