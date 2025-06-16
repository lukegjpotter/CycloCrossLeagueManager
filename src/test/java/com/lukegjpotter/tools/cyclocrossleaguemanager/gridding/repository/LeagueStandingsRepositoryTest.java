package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class LeagueStandingsRepositoryTest {

    @Autowired
    LeagueStandingsRepository leagueStandingsRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void loadDataFromLeagueStandingsGoogleSheet_CurrentYear_TopResult() throws IOException {
        LeagueStandingsRowRecord actual = leagueStandingsRepository.loadDataFromLeagueStandingsGoogleSheet(2).get(0);
        LeagueStandingsRowRecord expected = new LeagueStandingsRowRecord("A-Race", "Tadhg Killeen", "Kilcullen Cycling Club Murphy Geospacial", 235);

        assertEquals(expected, actual);
    }

    @Test
    void loadDataFromLeagueStandingsGoogleSheet_LastYear_TopTotal_NotAdjustedTotal() throws IOException {
        LeagueStandingsRowRecord actual = leagueStandingsRepository.loadDataFromLeagueStandingsGoogleSheet(1).get(0);
        LeagueStandingsRowRecord expected = new LeagueStandingsRowRecord("A-Race", "Javan Nulty", "Un-Attached Leinster", 268);

        assertEquals(expected, actual);
    }

    @Test
    void findLeaguePositionOfAllUngriddedSignups_withPreGriddedRidersInLeagueStandings() {
        List<RiderGriddingPositionRecord> actual = leagueStandingsRepository.findLeaguePositionOfAllUngriddedSignups(
                List.of(new LeagueStandingsRowRecord("A-Race", "James Jameson", "Willow CC", 123),
                        new LeagueStandingsRowRecord("A-Race", "John Johnson", "Wheel Wheelers", 122),
                        new LeagueStandingsRowRecord("A-Race", "Anders Andersen", "Potato CC", 121),
                        new LeagueStandingsRowRecord("A-Race", "Dean Harvey", "Trinity Racing", 120),
                        new LeagueStandingsRowRecord("Women", "Cindy Mindy", "Horse Wheelers", 123),
                        new LeagueStandingsRowRecord("Women", "Barbie Brannagh", "Anti-Cycle Cycle Club", 122),
                        new LeagueStandingsRowRecord("Women", "Maria Larkin", "Donkey Label", 121),
                        new LeagueStandingsRowRecord("Women", "Mary Contrary", "Shamrock CC", 120)),

                List.of(new BookingReportRowRecord("A-Race", "John Johnson", "Wheel Wheelers"),
                        new BookingReportRowRecord("A-Race", "James Jameson", "Willow CC"),
                        new BookingReportRowRecord("A-Race", "Dean Harvey", "Trinity Racing"),
                        new BookingReportRowRecord("Women", "Mary Contrary", "Shamrock CC"),
                        new BookingReportRowRecord("Women", "Barbie Brannagh", "Anti-Cycle Cycle Club"),
                        new BookingReportRowRecord("Women", "Maria Larkin", "Donkey Label"),
                        new BookingReportRowRecord("B-Race", "Billy Bracer", "Ligma CC")),

                List.of(new RiderGriddingPositionRecord("A-Race", 1, "Dean Harvey", "Trinity Racing"),
                        new RiderGriddingPositionRecord("Women", 1, "Maria Larkin", "Donkey Label")));

        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("A-Race", 2, "James Jameson", "Willow CC"),
                new RiderGriddingPositionRecord("A-Race", 3, "John Johnson", "Wheel Wheelers"),
                new RiderGriddingPositionRecord("Women", 2, "Barbie Brannagh", "Anti-Cycle Cycle Club"),
                new RiderGriddingPositionRecord("Women", 3, "Mary Contrary", "Shamrock CC"));

        assertEquals(expected, actual);
    }

    @Test
    void findLeaguePositionOfAllUngriddedSignups_withoutPreGriddedRiders() {
        List<RiderGriddingPositionRecord> actual = leagueStandingsRepository.findLeaguePositionOfAllUngriddedSignups(
                List.of(new LeagueStandingsRowRecord("A-Race", "James Jameson", "Willow CC", 123),
                        new LeagueStandingsRowRecord("A-Race", "John Johnson", "Wheel Wheelers", 122),
                        new LeagueStandingsRowRecord("A-Race", "Anders Andersen", "Potato CC", 121),
                        new LeagueStandingsRowRecord("A-Race", "Dean Harvey", "Trinity Racing", 120),
                        new LeagueStandingsRowRecord("Women", "Cindy Mindy", "Horse Wheelers", 123),
                        new LeagueStandingsRowRecord("Women", "Barbie Brannagh", "Anti-Cycle Cycle Club", 122),
                        new LeagueStandingsRowRecord("Women", "Maria Larkin", "Donkey Label", 121),
                        new LeagueStandingsRowRecord("Women", "Mary Contrary", "Shamrock CC", 120)),

                List.of(new BookingReportRowRecord("A-Race", "John Johnson", "Wheel Wheelers"),
                        new BookingReportRowRecord("A-Race", "James Jameson", "Willow CC"),
                        new BookingReportRowRecord("A-Race", "Dean Harvey", "Trinity Racing"),
                        new BookingReportRowRecord("Women", "Mary Contrary", "Shamrock CC"),
                        new BookingReportRowRecord("Women", "Barbie Brannagh", "Anti-Cycle Cycle Club"),
                        new BookingReportRowRecord("Women", "Maria Larkin", "Donkey Label"),
                        new BookingReportRowRecord("B-Race", "Billy Bracer", "Ligma CC")),
                new ArrayList<>());

        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("A-Race", 1, "James Jameson", "Willow CC"),
                new RiderGriddingPositionRecord("A-Race", 2, "John Johnson", "Wheel Wheelers"),
                new RiderGriddingPositionRecord("A-Race", 3, "Dean Harvey", "Trinity Racing"),
                new RiderGriddingPositionRecord("Women", 1, "Barbie Brannagh", "Anti-Cycle Cycle Club"),
                new RiderGriddingPositionRecord("Women", 2, "Maria Larkin", "Donkey Label"),
                new RiderGriddingPositionRecord("Women", 3, "Mary Contrary", "Shamrock CC"));


        assertEquals(expected, actual);
    }
}