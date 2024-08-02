package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.CycloCross24Record;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderUciPointRecord;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class UciPointsRepository {

    private static final Logger logger = LoggerFactory.getLogger(UciPointsRepository.class);

    public List<RiderGriddingPositionRecord> findRidersWithUciPointsWhoAreSignedUp(final List<BookingReportRowRecord> signupsBookingReportList) {

        // Find ME, MJ and WE UCI Points from Website with jSoup.
        final List<RiderUciPointRecord> ridersWithUciPoints = new ArrayList<>();
        List.of( // ToDo: make the "2023-2024" part of the URL a property.
                        new CycloCross24Record("ME", "https://cyclocross24.com/uciranking/2023-2024/ME/?country=Ireland"),
                        new CycloCross24Record("MJ", "https://cyclocross24.com/uciranking/2023-2024/MJ/?country=Ireland"),
                        new CycloCross24Record("WE", "https://cyclocross24.com/uciranking/2023-2024/WE/?country=Ireland"))
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

                                        riderFullName = riderProfilePageAttributes.get(0).text().trim()
                                                + " "
                                                + riderProfilePageAttributes.get(1).text().trim();
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

        // Are the UCI Pointed Riders in the list of signups?
        final List<RiderGriddingPositionRecord> griddedRidersWithUciPoints = new ArrayList<>();

        ridersWithUciPoints.forEach(riderUciPointRecord -> {
            String raceCategory = switch (riderUciPointRecord.uciCategory()) {
                case "ME", "MJ" -> "A-Race";
                case "WE" -> "Women";
                default -> throw new IllegalStateException("Unexpected value: " + riderUciPointRecord.uciCategory());
            };

            int gridPosition = griddedRidersWithUciPoints.stream()
                    .filter(riderGriddingPositionRecord -> riderGriddingPositionRecord.raceCategory().equals(raceCategory))
                    .sorted(Comparator.comparingInt(RiderGriddingPositionRecord::gridPosition).reversed())
                    .toList().get(0).gridPosition() + 1;

            if (signupsBookingReportList.contains(
                    new BookingReportRowRecord(raceCategory, riderUciPointRecord.fullName(), "")))
                griddedRidersWithUciPoints.add(
                        new RiderGriddingPositionRecord(raceCategory, gridPosition, riderUciPointRecord.fullName(), ""));
        });

        return griddedRidersWithUciPoints;
    }
}
