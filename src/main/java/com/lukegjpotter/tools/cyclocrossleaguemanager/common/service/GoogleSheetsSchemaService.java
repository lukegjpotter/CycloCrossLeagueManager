package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.GriddingRaceType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleSheetsSchemaService {

    public List<GriddingRaceType> griddingSchema() {
        return List.of(
                new GriddingRaceType("A-Race", "B4", 24),
                new GriddingRaceType("Women's Race", "E4", 24),
                new GriddingRaceType("B-Race", "H4", 24),
                new GriddingRaceType("Boy's U16", "B31", 8),
                new GriddingRaceType("Girl's U16", "E31", 8),
                new GriddingRaceType("Boy's U12", "H31", 8),
                new GriddingRaceType("Boy's U14", "B42", 8),
                new GriddingRaceType("Girl's U14", "E42", 8),
                new GriddingRaceType("Girl's U12", "H42", 8));
    }
}

