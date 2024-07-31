package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.CycloCross24Record;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderUciPointRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Repository
public class UciPointsRepository {

    private static final Logger logger = LoggerFactory.getLogger(UciPointsRepository.class);

    public List<RiderGriddingPositionRecord> findRidersWithUciPointsWhoAreSignedUp(final List<BookingReportRowRecord> signupsBookingReportList) {

        // Find ME, MJ and WE UCI Points from Website with jSoup.
        final List<RiderUciPointRecord> ridersWithUciPoints = new ArrayList<>();
        List.of(
                        new CycloCross24Record("ME", "https://cyclocross24.com/uciranking/2023-2024/ME/?country=Ireland"),
                        new CycloCross24Record("MJ", "https://cyclocross24.com/uciranking/2023-2024/MJ/?country=Ireland"),
                        new CycloCross24Record("WE", "https://cyclocross24.com/uciranking/2023-2024/WE/?country=Ireland"))
                .forEach(cycloCross24Record -> {
                    try {
                        Document cycloCross24 = Jsoup.connect(cycloCross24Record.standingsUrl()).get();
                        Elements uciPointsStandingsTableRows = cycloCross24.select("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
                        uciPointsStandingsTableRows.forEach(tableRow -> {
                            // ToDo: Open rider Profile Page, and get their correctly formatted name.
                            List<String> riderNameTokenized = new ArrayList<>(Arrays.stream(tableRow.getElementsByTag("td").get(3).text().trim().split(" ")).toList());
                            int uciPoints = Integer.parseInt(tableRow.getElementsByTag("td").get(6).text().trim());

                            riderNameTokenized.add(0, riderNameTokenized.get(riderNameTokenized.size() - 1));
                            riderNameTokenized.remove(riderNameTokenized.size() - 1);
                            StringBuilder riderNameFormatted = new StringBuilder();
                            riderNameTokenized.forEach(token -> riderNameFormatted.append(token).append(" "));

                            ridersWithUciPoints.add(new RiderUciPointRecord(cycloCross24Record.uciCode(), riderNameFormatted.toString().trim(), uciPoints));
                        });

                    } catch (IOException e) {
                        logger.error("Error getting Men's Elite UCI Points. Error: {}", e.getMessage());
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

            if (signupsBookingReportList.contains(new BookingReportRowRecord(raceCategory, riderUciPointRecord.fullName(), "")))
                griddedRidersWithUciPoints.add(new RiderGriddingPositionRecord(raceCategory, gridPosition, riderUciPointRecord.fullName(), ""));
        });

        return griddedRidersWithUciPoints;
    }
}
