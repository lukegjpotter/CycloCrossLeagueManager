package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingResultRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GriddingRepositoryTest {

    @Autowired
    GriddingRepository griddingRepository;
    @Autowired
    GoogleSheetsService googleSheetsService;

    @BeforeEach
    void setUp() {
    }

    // ToDo: Comment out this Annotation to enable output monitoring. But remember to manually clean out the file.
    @AfterEach
    void tearDown() {
        griddingRepository.writeGriddingToGoogleSheet(
                new ArrayList<>(List.of(new RiderGriddingPositionRecord("A-Race", 1, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 2, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 3, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 4, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 5, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 6, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 7, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 1, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 2, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 3, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 4, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 5, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 6, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 7, "", ""),
                        new RiderGriddingPositionRecord("Women's Race", 1, "", ""),
                        new RiderGriddingPositionRecord("Women's Race", 2, "", ""),
                        new RiderGriddingPositionRecord("Women's Race", 3, "", ""),
                        new RiderGriddingPositionRecord("Women's Race", 4, "", ""),
                        new RiderGriddingPositionRecord("Women's Race", 5, "", ""),
                        new RiderGriddingPositionRecord("Women's Race", 6, "", ""),
                        new RiderGriddingPositionRecord("Women's Race", 7, "", ""),
                        new RiderGriddingPositionRecord("Boy's U16", 1, "", ""),
                        new RiderGriddingPositionRecord("Boy's U16", 2, "", ""),
                        new RiderGriddingPositionRecord("Boy's U16", 3, "", ""),
                        new RiderGriddingPositionRecord("Boy's U16", 4, "", ""),
                        new RiderGriddingPositionRecord("Boy's U14", 1, "", ""),
                        new RiderGriddingPositionRecord("Boy's U14", 2, "", ""),
                        new RiderGriddingPositionRecord("Boy's U14", 3, "", ""),
                        new RiderGriddingPositionRecord("Boy's U14", 4, "", ""),
                        new RiderGriddingPositionRecord("Boy's U12", 1, "", ""),
                        new RiderGriddingPositionRecord("Boy's U12", 2, "", ""),
                        new RiderGriddingPositionRecord("Boy's U12", 3, "", ""),
                        new RiderGriddingPositionRecord("Boy's U12", 4, "", ""),
                        new RiderGriddingPositionRecord("Girl's U16", 1, "", ""),
                        new RiderGriddingPositionRecord("Girl's U16", 2, "", ""),
                        new RiderGriddingPositionRecord("Girl's U16", 3, "", ""),
                        new RiderGriddingPositionRecord("Girl's U16", 4, "", ""),
                        new RiderGriddingPositionRecord("Girl's U14", 1, "", ""),
                        new RiderGriddingPositionRecord("Girl's U14", 2, "", ""),
                        new RiderGriddingPositionRecord("Girl's U14", 3, "", ""),
                        new RiderGriddingPositionRecord("Girl's U14", 4, "", ""),
                        new RiderGriddingPositionRecord("Girl's U12", 1, "", ""),
                        new RiderGriddingPositionRecord("Girl's U12", 2, "", ""),
                        new RiderGriddingPositionRecord("Girl's U12", 3, "", ""),
                        new RiderGriddingPositionRecord("Girl's U12", 4, "", ""))),
                "https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/");
    }

    @Test
    @Order(1)
    void writeGriddingToGoogleSheet_CompareReturns() {
        GriddingResultRecord actual = griddingRepository.writeGriddingToGoogleSheet(
                new ArrayList<>(List.of(new RiderGriddingPositionRecord("A-Race", 1, "UCI Points Leader", "Team Sponsor"),
                        new RiderGriddingPositionRecord("Women", 2, "League Leader", "Club Clubbers"),
                        new RiderGriddingPositionRecord("Women", 1, "UCI Points Holder", "Team Pro"),
                        new RiderGriddingPositionRecord("A-Race", 3, "High League Place", "Speedy CC"),
                        new RiderGriddingPositionRecord("A-Race", 2, "UCI Points Holder", "National CC"))),
                "https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/edit?usp=sharing");
        GriddingResultRecord expected = new GriddingResultRecord("https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/", "");

        assertEquals(expected, actual);
    }

    @Test
    void writeGriddingToGoogleSheet_CompareValues() throws IOException {
        griddingRepository.writeGriddingToGoogleSheet(
                new ArrayList<>(List.of(new RiderGriddingPositionRecord("A-Race", 1, "UCI Points Leader", "Team Sponsor"),
                        new RiderGriddingPositionRecord("Women", 2, "League Leader", "Club Clubbers"),
                        new RiderGriddingPositionRecord("Women", 1, "UCI Points Holder", "Team Pro"),
                        new RiderGriddingPositionRecord("A-Race", 3, "High League Place", "Speedy CC"),
                        new RiderGriddingPositionRecord("A-Race", 2, "UCI Points Holder", "National CC"))),
                "https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/");

        // Compare Values
        StringBuilder actualRaceGridding = new StringBuilder();
        ValueRange actualARaceValueRangeResponse = googleSheetsService.readSpreadsheetValuesInRange("1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec", "Gridding!B4:C6");
        List<List<Object>> actualARaceValues = actualARaceValueRangeResponse.getValues();
        for (List<Object> row : actualARaceValues) {
            actualRaceGridding.append(String.format("%s, %s\n", row.get(0), row.get(1)));
        }
        ValueRange actualWomensRaceValueRangeResponse = googleSheetsService.readSpreadsheetValuesInRange("1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec", "Gridding!E4:F5");
        List<List<Object>> actualWomensRaceValues = actualWomensRaceValueRangeResponse.getValues();
        for (List<Object> row : actualWomensRaceValues) {
            actualRaceGridding.append(String.format("%s, %s\n", row.get(0), row.get(1)));
        }

        String expectedRaceGridding = """
                UCI Points Leader, Team Sponsor
                UCI Points Holder, National CC
                High League Place, Speedy CC
                UCI Points Holder, Team Pro
                League Leader, Club Clubbers
                """;

        assertEquals(expectedRaceGridding, actualRaceGridding.toString());
    }

    @Test
    void writeGriddingToGoogleSheet_ExceedMaxAllowableGridded() throws IOException {
        // Given and When
        griddingRepository.writeGriddingToGoogleSheet(
                new ArrayList<>(List.of(
                        new RiderGriddingPositionRecord("Under 16s Male", 1, "LeagueLeader", "Team Sponsor"),
                        new RiderGriddingPositionRecord("Under 16s Male", 2, "Second", "Club Clubbers"),
                        new RiderGriddingPositionRecord("Under 16s Male", 3, "Third", "Team Pro"),
                        new RiderGriddingPositionRecord("Under 16s Male", 4, "Fourth", "Speedy CC"),
                        new RiderGriddingPositionRecord("Under 16s Male", 5, "Fifth", "National CC"),
                        new RiderGriddingPositionRecord("Under 16s Male", 6, "Sixth", "Team Pro"),
                        new RiderGriddingPositionRecord("Under 16s Male", 7, "Seventh", "Speedy CC"),
                        new RiderGriddingPositionRecord("Under 16s Male", 8, "Eighth", "National CC"),
                        new RiderGriddingPositionRecord("Under 16s Male", 9, "Ninth No Grid", "Speedy CC"),
                        new RiderGriddingPositionRecord("Under 16s Male", 10, "Tenth No Grid", "National CC"))),
                "https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/");

        // Then - Compare Values
        StringBuilder actualRaceGridding = new StringBuilder();

        ValueRange actualU16RaceValueRangeResponse = googleSheetsService.readSpreadsheetValuesInRange("1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec", "Gridding!B31:C40");
        List<List<Object>> actualU16RaceValues = actualU16RaceValueRangeResponse.getValues();
        for (List<Object> row : actualU16RaceValues) {
            try {
                actualRaceGridding.append(String.format("%s, %s\n", row.get(0), row.get(1)));
            } catch (IndexOutOfBoundsException e) {
                actualRaceGridding.append(",\n");
            }
        }

        String expectedRaceGridding = """
                LeagueLeader, Team Sponsor
                Second, Club Clubbers
                Third, Team Pro
                Fourth, Speedy CC
                Fifth, National CC
                Sixth, Team Pro
                Seventh, Speedy CC
                Eighth, National CC
                ,
                Name, Club
                """;

        assertEquals(expectedRaceGridding, actualRaceGridding.toString());
    }
}