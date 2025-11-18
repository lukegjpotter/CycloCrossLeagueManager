package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.CycloCross24Record;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderUciPointRecord;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class UciPointsRepository {

    private static final Logger logger = LoggerFactory.getLogger(UciPointsRepository.class);
    @Value("${gridding.ucirankingsyear}")
    private String uciRankingsYear;

    public List<RiderGriddingPositionRecord> findRidersWithUciPointsWhoAreSignedUp(final List<BookingReportRowRecord> signupsBookingReportList) {

        logger.info("Finding Riders with UCI Points who are signed up.");

        // Find ME, MJ and WE UCI Points from Website with jSoup.
        final List<RiderUciPointRecord> ridersWithUciPoints = new ArrayList<>();
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
                                        String surname = riderProfilePageAttributes.get(1).text().replace("'", " ").trim();
                                        if (surname.startsWith("Mc")) {
                                            surname = "Mc " + surname.substring(2);
                                        }
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

        // Are the riders with UCI Points in the list of signups?
        final List<RiderGriddingPositionRecord> griddedRidersWithUciPoints = new ArrayList<>();
        final List<String> namesOfRidersSignedUp = signupsBookingReportList.stream().map(BookingReportRowRecord::fullName).toList();

        logger.trace("Riders with UCI Points: {}", ridersWithUciPoints.size());

        for (RiderUciPointRecord riderUciPointRecord : ridersWithUciPoints) {
            if (!namesOfRidersSignedUp.contains(riderUciPointRecord.fullName())) continue;
            logger.trace("Rider with UCI Points: {}", riderUciPointRecord.fullName());

            String raceCategory = switch (riderUciPointRecord.uciCategory()) {
                case "ME", "MJ" -> "A-Race";
                case "WE" -> "Women";
                default -> throw new IllegalStateException("Unexpected value: " + riderUciPointRecord.uciCategory());
            };

            List<RiderGriddingPositionRecord> ridersInGriddedOrderForRaceCategory = griddedRidersWithUciPoints.stream()
                    .filter(riderGriddingPositionRecord -> riderGriddingPositionRecord.raceCategory().startsWith(raceCategory))
                    .sorted(Comparator.comparingInt(RiderGriddingPositionRecord::gridPosition).reversed())
                    .toList();

            int gridPosition = (ridersInGriddedOrderForRaceCategory.isEmpty()) ? 1 : ridersInGriddedOrderForRaceCategory.get(0).gridPosition() + 1;

            for (BookingReportRowRecord signup : signupsBookingReportList) {
                if (signup.raceCategory().startsWith(raceCategory) && signup.fullName().equalsIgnoreCase(riderUciPointRecord.fullName())) {
                    griddedRidersWithUciPoints.add(new RiderGriddingPositionRecord(raceCategory, gridPosition, riderUciPointRecord.fullName(), signup.clubName()));
                    break;
                }
            }
        }

        logger.info("Riders with UCI Points who are signed up: {}.", griddedRidersWithUciPoints.size());
        logger.trace("Signed Up riders with UCI Points: {}.", griddedRidersWithUciPoints);

        return griddedRidersWithUciPoints;
    }
}
