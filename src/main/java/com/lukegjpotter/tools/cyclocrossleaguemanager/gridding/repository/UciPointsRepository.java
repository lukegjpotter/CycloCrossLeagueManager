package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.component.TextUtilsComponent;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.component.UciPointsWebsiteScraperComponent;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderUciPointRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class UciPointsRepository {

    private static final Logger logger = LoggerFactory.getLogger(UciPointsRepository.class);
    @Value("${gridding.ucirankingsyear}")
    private String uciRankingsYear;
    private final TextUtilsComponent textUtilsComponent;
    private final UciPointsWebsiteScraperComponent uciPointsWebsiteScraperComponent;

    @Autowired
    public UciPointsRepository(TextUtilsComponent textUtilsComponent, UciPointsWebsiteScraperComponent uciPointsWebsiteScraperComponent) {
        this.textUtilsComponent = textUtilsComponent;
        this.uciPointsWebsiteScraperComponent = uciPointsWebsiteScraperComponent;
    }

    public List<RiderGriddingPositionRecord> findRidersWithUciPointsWhoAreSignedUp(final List<BookingReportRowRecord> signupsBookingReportList) {

        logger.info("Finding Riders with UCI Points who are signed up.");

        // Find ME, MJ and WE UCI Points from Website with jSoup.
        final List<RiderUciPointRecord> ridersWithUciPoints = uciPointsWebsiteScraperComponent.getIrishRidersWithUciPointsForYear(uciRankingsYear);

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
