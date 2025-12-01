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
        leagueStandingsWriteRepository.updateLeagueStandingsGoogleSheetWithRaceResults(
                "1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw", 5, new HashMap<>(Map.of(
                        "A-RaceTadhg KilleenKilcullen Cycling Club Murphy Geospacial", new ResultRowRecord("A-Race", 1, "Tadhg Killeen", "Kilcullen Cycling Club Murphy Geospacial", "U23", "Male"),
                        "A-RaceConor ReganKilcullen Cycling Club Murphy Geospacial", new ResultRowRecord("A-Race", 2, "Conor Regan", "Kilcullen Cycling Club Murphy Geospacial", "Junior", "Male"),
                        "A-RaceRuairi ByrneUCD Cycling Club", new ResultRowRecord("A-Race", 3, "Ruairi Byrne", "UCD Cycling Club", "U23", "Male"),
                        "A-RaceTest RiderTest Club", new ResultRowRecord("A-Race", 4, "Test Rider", "Test Club", "M50", "Male"),
                        "A-RaceRobin SeymourTeam WORC", new ResultRowRecord("A-Race", 5, "Robin Seymour", "Team WORC", "M50", "Male")
                )));

        String[] actual = testUtils.aRaceStandingsToString("1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw").split("\n");
        String expected = """
                1, Conor Regan, Kilcullen Cycling Club Murphy Geospacial, Junior, 60, 51, 60, 55, 55, 281, 230
                2, Brian Melia, Team WORC, M40, 45, 46, 45, 37, 0, 173, 173
                """; // ToDo: Populate More

        assertEquals(expected, actual[0] + "\n" + actual[1] + "\n");

        // Clean Up
        testUtils.wipeStandingsSheetResultsColumnAndNames("1az6xrS_QpnK3Wc8lHHVUcqvPvHZYbD243X2H9LpNjkw", "I", List.of(new RiderNameAndCellRecord("A-Race", "Test Rider", "Test Club", "")));
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