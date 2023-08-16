package common.service;

import common.model.TransformedResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import results.service.ResultsExtractorService;
import standings.service.StandingsLoaderService;

@Service
public class ResultsAndStandingsService {

    private static final Logger logger = LoggerFactory.getLogger(ResultsAndStandingsService.class);

    @Autowired
    private ResultsExtractorService resultsExtractorService;
    @Autowired
    private StandingsLoaderService standingsLoaderService;

    public String performETL(String raceResultsUrl) {
        // ToDo: Convert to URL

        TransformedResults transformedResults = resultsExtractorService.extract(raceResultsUrl);
        standingsLoaderService.load(transformedResults);

        return "";
    }
}
