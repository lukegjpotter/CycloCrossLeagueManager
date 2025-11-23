package com.lukegjpotter.tools.cyclocrossleaguemanager.results.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.TransformedResults;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResultsExtractorService {

    private static final Logger logger = LoggerFactory.getLogger(ResultsExtractorService.class);

    public TransformedResults extract(final String raceResultsUrl) {
        // TODO: Start jSoup Extracting RaceResults results webpage.
        Document document = null;
        //document = Jsoup.connect(raceResultsUrl).get();
        TransformedResults transformedResults = new TransformedResults();
        //String round = document.body().getElementsByTag("thbody").get(0).getElementsByTag("tr").get(1).text().trim();
        transformedResults.setRound("");

        return transformedResults;
    }
}
