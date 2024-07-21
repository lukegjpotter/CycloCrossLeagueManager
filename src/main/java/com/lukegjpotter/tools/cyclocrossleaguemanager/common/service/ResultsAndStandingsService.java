package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.TransformedResults;
import com.lukegjpotter.tools.cyclocrossleaguemanager.results.service.ResultsExtractorService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.service.StandingsLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResultsAndStandingsService {

    private static final Logger logger = LoggerFactory.getLogger(ResultsAndStandingsService.class);

    private ResultsExtractorService resultsExtractorService;
    private StandingsLoaderService standingsLoaderService;

    public ResultsAndStandingsService(ResultsExtractorService resultsExtractorService, StandingsLoaderService standingsLoaderService) {
        this.resultsExtractorService = resultsExtractorService;
        this.standingsLoaderService = standingsLoaderService;
    }

    public String performETL(String raceResultsUrl) {
        // ToDo: Convert to URL

        TransformedResults transformedResults = resultsExtractorService.extract(raceResultsUrl);
        standingsLoaderService.load(transformedResults);

        return "";
    }
}
