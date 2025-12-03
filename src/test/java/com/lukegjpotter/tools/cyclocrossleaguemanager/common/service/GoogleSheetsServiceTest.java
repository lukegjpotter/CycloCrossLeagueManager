package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.testutils.StandingsTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GoogleSheetsServiceTest {

    @Autowired
    GoogleSheetsService googleSheetsService;
    @Autowired
    StandingsTestUtils testUtils;

    @Test
    void sortSpreadsheetOnColumns() throws IOException {
        googleSheetsService.sortSpreadsheetOnColumns("1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw", List.of("E", "F", "G"));

        String[] aRaceStandings = testUtils.aRaceStandingsToString("1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw").split("\n");
        String actual = aRaceStandings[0] + "\n" + aRaceStandings[1] + "\n" + aRaceStandings[2];
        String expected = """
                1, Conor Regan, Kilcullen Cycling Club Murphy Geospacial, Junior, 60, 51, 60, 55, 0, 226, 226
                2, Sean Lundy, UCD Cycling Club, Senior, 55, 48, 55, 3, 0, 161, 161
                3, Kevin KEANE, St. Tiernan's Cycling Club, M40, 0, 0, 51, 44, 0, 95, 95""";

        assertEquals(expected, actual);

        // Clean Up.
        googleSheetsService.sortSpreadsheetOnColumns("1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw", List.of("E", "F", "G", "H", "I", "J", "K"));
    }
}