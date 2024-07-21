package results.service;

import common.model.TransformedResults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ResultsExtractorService {

    private static final Logger logger = LoggerFactory.getLogger(ResultsExtractorService.class);

    public TransformedResults extract(String raceResultsUrl) {
        // TODO: Start jSoup Extracting RaceResults results webpage.
        Document document = null;
        try {
            document = Jsoup.connect(raceResultsUrl).get();
        } catch (IOException e) {
            return null;
        }
        TransformedResults transformedResults = new TransformedResults();
        String round = document.body().getElementsByTag("thbody").get(0).getElementsByTag("tr").get(1).text().trim();
        transformedResults.setRound(round);

        return transformedResults;
    }
}
