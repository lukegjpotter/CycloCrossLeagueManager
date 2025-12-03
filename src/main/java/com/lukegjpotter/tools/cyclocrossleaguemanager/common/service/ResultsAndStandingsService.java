package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.TransformedResults;
import com.lukegjpotter.tools.cyclocrossleaguemanager.results.service.ResultsExtractorService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.service.StandingsUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
// ToDo: Delete Class.
public class ResultsAndStandingsService {

    private static final Logger logger = LoggerFactory.getLogger(ResultsAndStandingsService.class);
    private final ResultsExtractorService resultsExtractorService;
    private final StandingsUpdaterService standingsUpdaterService;

    @Autowired
    public ResultsAndStandingsService(ResultsExtractorService resultsExtractorService, StandingsUpdaterService standingsUpdaterService) {
        this.resultsExtractorService = resultsExtractorService;
        this.standingsUpdaterService = standingsUpdaterService;
    }

    public String performETL(final String raceResultsUrl) {
        // ToDo: Convert to URL

        TransformedResults transformedResults = resultsExtractorService.extract(raceResultsUrl);
        //standingsUpdaterService.load(transformedResults);

        return "";
    }
}
