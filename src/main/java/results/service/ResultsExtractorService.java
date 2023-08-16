package results.service;

import common.model.TransformedResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResultsExtractorService {

    private static final Logger logger = LoggerFactory.getLogger(ResultsExtractorService.class);

    public TransformedResults extract(String raceResultsUrl) {
        // TODO: Start jSoup Extracting RaceResults results webpage.
        return null;
    }
}
