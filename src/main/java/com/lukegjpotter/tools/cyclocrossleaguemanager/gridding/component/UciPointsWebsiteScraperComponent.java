package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.component;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.component.TextUtilsComponent;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.CycloCross24Record;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderUciPointRecord;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UciPointsWebsiteScraperComponent {

    private static final Logger logger = LoggerFactory.getLogger(UciPointsWebsiteScraperComponent.class);

    private final TextUtilsComponent textUtilsComponent;

    @Autowired
    public UciPointsWebsiteScraperComponent(TextUtilsComponent textUtilsComponent) {
        this.textUtilsComponent = textUtilsComponent;
    }

    /**
     * Uses jSoup to scrape CycloCross24's website.
     *
     * @param uciRankingsYear e.g. "2025-2026"
     * @return List<RiderUciPointRecord>
     */
    public List<RiderUciPointRecord> getIrishRidersWithUciPointsForYear(String uciRankingsYear) {
        List<RiderUciPointRecord> ridersWithUciPoints = new ArrayList<>();

        final String uciRankingsUrlStub = "https://cyclocross24.com/uciranking/";

        List.of(
                        new CycloCross24Record("ME", uciRankingsUrlStub + uciRankingsYear + "/ME/?country=Ireland"),
                        new CycloCross24Record("MJ", uciRankingsUrlStub + uciRankingsYear + "/MJ/?country=Ireland"),
                        new CycloCross24Record("WE", uciRankingsUrlStub + uciRankingsYear + "/WE/?country=Ireland"))
                .forEach(cycloCross24Record -> {
                    try {
                        Jsoup.connect(cycloCross24Record.standingsUrl()).get().selectFirst("table")
                                .getElementsByTag("tbody").get(0).getElementsByTag("tr")
                                .forEach(tableRow -> {

                                    String riderFullName = "";
                                    String cyclocross24BaseUrl = "https://cyclocross24.com";
                                    String urlSlugToRiderProfile = tableRow.getElementsByTag("td").get(3)
                                            .getElementsByTag("a").get(0).attribute("href").getValue();
                                    try {
                                        Elements riderProfilePageAttributes = Jsoup.connect(
                                                        cyclocross24BaseUrl + urlSlugToRiderProfile).get()
                                                .getElementsByClass("riderinfo-table").get(0)
                                                .getElementsByTag("tbody").get(0)
                                                .getElementsByTag("td");

                                        String firstName = riderProfilePageAttributes.get(0).text().trim();

                                        // Convert CX24's Surnames to CyclingIreland's Surnames.
                                        String surname = textUtilsComponent.toIrishFormattedNameAndTitleCase(riderProfilePageAttributes.get(1).text().trim());

                                        riderFullName = firstName + " " + surname;
                                    } catch (IOException e) {
                                        logger.error("Error on Rider Profile Page. Error: {}", e.getMessage());
                                    }

                                    int uciPoints = Integer.parseInt(tableRow.getElementsByTag("td").get(6)
                                            .text().trim());

                                    ridersWithUciPoints.add(new RiderUciPointRecord(
                                            cycloCross24Record.uciCode(), riderFullName, uciPoints));
                                });

                    } catch (IOException e) {
                        logger.error("Error getting UCI Points. Error: {}", e.getMessage());
                    }
                });

        return ridersWithUciPoints;
    }
}
