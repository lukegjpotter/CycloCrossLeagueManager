package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.exception.UpdateStandingsException;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.ResultRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.RiderNameAndCellRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.testutils.StandingsTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LeagueStandingsWriteRepositoryTest {

    @Autowired
    LeagueStandingsWriteRepository leagueStandingsWriteRepository;
    @Autowired
    StandingsTestUtils testUtils;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateLeagueStandingsGoogleSheetWithRaceResults_round5() throws IOException {
        String standingsSpreadSheetId = "1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw";

        leagueStandingsWriteRepository.updateLeagueStandingsGoogleSheetWithRaceResults(
                standingsSpreadSheetId, 5, new HashMap<>(Map.of(
                        "A-RaceTadhg KilleenKilcullen Cycling Club Murphy Geospacial", new ResultRowRecord("A-Race", 1, "Tadhg Killeen", "Kilcullen Cycling Club Murphy Geospacial", "U23", "Male"),
                        "A-RaceConor ReganKilcullen Cycling Club Murphy Geospacial", new ResultRowRecord("A-Race", 2, "Conor Regan", "Kilcullen Cycling Club Murphy Geospacial", "Junior", "Male"),
                        "A-RaceRuairi ByrneUCD Cycling Club", new ResultRowRecord("A-Race", 3, "Ruairi Byrne", "UCD Cycling Club", "U23", "Male"),
                        "A-RaceTest RiderTest Club", new ResultRowRecord("A-Race", 4, "Test Rider", "Test Club", "M50", "Male"),
                        "A-RaceRobin SeymourTeam WORC", new ResultRowRecord("A-Race", 5, "Robin Seymour", "Team WORC", "M50", "Male"),
                        "A-RaceSecond TestriderTest Club", new ResultRowRecord("A-Race", 6, "Second Testrider", "Test Club", "M50", "Male"),
                        "WomenTest WomanTest Wheelers", new ResultRowRecord("Women", 1, "Test Woman", "Test Wheelers", "U23", "Female"),
                        "U16 MaleTest YouthTest CC", new ResultRowRecord("U16 Male", 1, "Test Youth", "Test CC", "U16", "Male")
                )));

        String[] aRaceStandings = testUtils.aRaceStandingsToString(standingsSpreadSheetId).split("\n");
        String[] womenStandings = testUtils.womensRaceStandingsToString(standingsSpreadSheetId).split("\n");
        String[] u16MaleStandings = testUtils.u16MaleStandingsToString(standingsSpreadSheetId).split("\n");
        String actual = "A-Race Standings\n"
                + aRaceStandings[0] + "\n"
                + aRaceStandings[10] + "\n"
                + "Women Standings\n"
                + womenStandings[12] + "\n"
                + "U16 Male Standings\n"
                + u16MaleStandings[9];

        String expected = """
                A-Race Standings
                1, Conor Regan, Kilcullen Cycling Club Murphy Geospacial, Junior, 60, 51, 60, 55, 55, 281, 230
                11, Tadhg Killeen, Kilcullen Cycling Club Murphy Geospacial, U23, 0, 0, 0, 60, 60, 120, 120
                Women Standings
                13, Test Woman, Test Wheelers, U23, 0, 0, 0, 0, 60, 60, 60
                U16 Male Standings
                10, Test Youth, Test CC, U16, 0, 0, 0, 0, 60, 60, 60""";

        assertEquals(expected, actual);

        // Clean Up
        testUtils.wipeStandingsSheetResultsColumnAndNames(standingsSpreadSheetId, "I", List.of(
                new RiderNameAndCellRecord("A-Race", "Test Rider", "Test Club", ""),
                new RiderNameAndCellRecord("A-Race", "Second Testrider", "Test Club", ""),
                new RiderNameAndCellRecord("Women", "Test Woman", "Test Wheelers", ""),
                new RiderNameAndCellRecord("U16 Male", "Test Youth", "Test CC", "")
        ));
        testUtils.sortStandingsSheet(standingsSpreadSheetId, "A-Race");
        testUtils.sortStandingsSheet(standingsSpreadSheetId, "Women");
        testUtils.sortStandingsSheet(standingsSpreadSheetId, "U16 Male");
    }

    @Test
    void updateLeagueStandingsGoogleSheetWithRaceResults_failFast() {
        assertThrows(UpdateStandingsException.class, () -> leagueStandingsWriteRepository.updateLeagueStandingsGoogleSheetWithRaceResults("", 2, new HashMap<>(Map.of(
                "A-RaceRider NameRiderClub CC", new ResultRowRecord("A-Race", 1, "Rider Name", "RiderClubCC", "U23", "Male")))));

        assertThrows(UpdateStandingsException.class, () -> leagueStandingsWriteRepository.updateLeagueStandingsGoogleSheetWithRaceResults("1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw", 0, new HashMap<>(Map.of(
                "A-RaceRider NameRiderClub CC", new ResultRowRecord("A-Race", 1, "Rider Name", "RiderClubCC", "U23", "Male")))));

        assertThrows(UpdateStandingsException.class, () -> leagueStandingsWriteRepository.updateLeagueStandingsGoogleSheetWithRaceResults("1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw", 2, new HashMap<>()));
    }
}