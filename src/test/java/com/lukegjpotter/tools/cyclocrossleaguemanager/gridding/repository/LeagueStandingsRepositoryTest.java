package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.testutils.GriddingTestMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class LeagueStandingsRepositoryTest {

    @Autowired
    LeagueStandingsRepository leagueStandingsRepository;
    @Autowired
    private GriddingTestMocks griddingTestMocks;

    @MockitoBean
    private GoogleSheetsService googleSheetsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void loadDataFromLeagueStandingsGoogleSheet_CurrentYear_TopResult() throws IOException {
        when(googleSheetsService.getSpreadsheetHeaders(anyString(), anyString())).thenReturn(griddingTestMocks.getLeagueStandings2024Headers());
        when(googleSheetsService.readSpreadsheetValuesInRange(anyString(), anyString())).thenReturn(griddingTestMocks.getLeagueStandings2024());

        LeagueStandingsRowRecord actual = leagueStandingsRepository.loadDataFromLeagueStandingsGoogleSheet(2).get(0);
        LeagueStandingsRowRecord expected = new LeagueStandingsRowRecord("A-Race", "Tadhg Killeen", "Kilcullen Cycling Club Murphy Geospacial", 235);

        assertEquals(expected, actual);
    }

    @Test
    void loadDataFromLeagueStandingsGoogleSheet_LastYear_TopTotal_NotAdjustedTotal() throws IOException {
        when(googleSheetsService.getSpreadsheetHeaders(anyString(), anyString())).thenReturn(griddingTestMocks.getLeagueStandings2023Headers());
        when(googleSheetsService.readSpreadsheetValuesInRange(anyString(), anyString())).thenReturn(griddingTestMocks.getLeagueStandings2023());

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

    @Test
    void findLeaguePositionOfAllUngriddedSignups_upperCaseNames() {
        List<RiderGriddingPositionRecord> actual = leagueStandingsRepository.findLeaguePositionOfAllUngriddedSignups(
                List.of(new LeagueStandingsRowRecord("A-Race", "James JAMESON", "Willow CC", 123),
                        new LeagueStandingsRowRecord("A-Race", "JOHN Johnson", "Wheel Wheelers", 122)),

                List.of(new BookingReportRowRecord("A-Race", "John Johnson", "Wheel Wheelers"),
                        new BookingReportRowRecord("A-Race", "James Jameson", "Willow CC")),
                new ArrayList<>());

        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("A-Race", 1, "James JAMESON", "Willow CC"),
                new RiderGriddingPositionRecord("A-Race", 2, "JOHN Johnson", "Wheel Wheelers"));

        assertEquals(expected, actual);
    }

    @Test
    void findLeaguePositionOfAllUngriddedSignups_withRaceCategorySuffixes() {
        List<RiderGriddingPositionRecord> actual = leagueStandingsRepository.findLeaguePositionOfAllUngriddedSignups(
                List.of(new LeagueStandingsRowRecord("A-Race", "James Jameson", "Willow CC", 123),
                        new LeagueStandingsRowRecord("A-Race", "John Johnson", "Wheel Wheelers", 122),
                        new LeagueStandingsRowRecord("A-Race", "Anders Andersen", "Potato CC", 121),
                        new LeagueStandingsRowRecord("A-Race", "Dean Harvey", "Trinity Racing", 120),
                        new LeagueStandingsRowRecord("Women", "Cindy Mindy", "Horse Wheelers", 123),
                        new LeagueStandingsRowRecord("Women", "Barbie Brannagh", "Anti-Cycle Cycle Club", 122),
                        new LeagueStandingsRowRecord("Women", "Maria Larkin", "Donkey Label", 121),
                        new LeagueStandingsRowRecord("Women", "Mary Contrary", "Shamrock CC", 120),
                        new LeagueStandingsRowRecord("Under 16s Female", "Mary Youth", "Shamrock CC", 120),
                        new LeagueStandingsRowRecord("B-Race", "Billy Bracer", "Ligma CC", 123)),

                List.of(new BookingReportRowRecord("A-Race", "John Johnson", "Wheel Wheelers"),
                        new BookingReportRowRecord("A-Race", "James Jameson", "Willow CC"),
                        new BookingReportRowRecord("A-Race", "Dean Harvey", "Trinity Racing"),
                        new BookingReportRowRecord("Women", "Mary Contrary", "Shamrock CC"),
                        new BookingReportRowRecord("Women", "Barbie Brannagh", "Anti-Cycle Cycle Club"),
                        new BookingReportRowRecord("Women", "Maria Larkin", "Donkey Label"),
                        new BookingReportRowRecord("Under 16s Female", "Mary Youth", "Shamrock CC"),
                        new BookingReportRowRecord("B-Race", "Billy Bracer", "Ligma CC")),

                List.of(new RiderGriddingPositionRecord("A-Race", 1, "Dean Harvey", "Trinity Racing")));

        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("A-Race", 2, "James Jameson", "Willow CC"),
                new RiderGriddingPositionRecord("A-Race", 3, "John Johnson", "Wheel Wheelers"),
                new RiderGriddingPositionRecord("Women", 1, "Barbie Brannagh", "Anti-Cycle Cycle Club"),
                new RiderGriddingPositionRecord("Women", 2, "Maria Larkin", "Donkey Label"),
                new RiderGriddingPositionRecord("Women", 3, "Mary Contrary", "Shamrock CC"),
                new RiderGriddingPositionRecord("Under 16s Female", 1, "Mary Youth", "Shamrock CC"),
                new RiderGriddingPositionRecord("B-Race", 1, "Billy Bracer", "Ligma CC"));


        assertEquals(expected, actual);
    }

    /**
     * Tests Riders who have aged up, or who have been upgraded from the B-Race.
     * It simulates The First Round of the League in the way that riders age up.
     * It also simulates normal league operations, when B-Racers are upgraded to the A-Race.
     */
    @Test
    void findLeaguePositionOfAllUngriddedSignups_UpgradedAndAgedUpRiders() {

        List<LeagueStandingsRowRecord> leagueStandings = List.of(
                new LeagueStandingsRowRecord("A-Race", "ARacerWith LeaguePoints", "Shamrockers", 38),
                new LeagueStandingsRowRecord("A-Race", "NonAttender ARacer", "Sleepy CC", 30),
                new LeagueStandingsRowRecord("B-Race", "Upgraded from B-Race", "XYZ CC", 55),
                new LeagueStandingsRowRecord("B-Race", "BRacerWith LeaguePoints", "Setanta CC", 35),
                new LeagueStandingsRowRecord("Women", "WomanWith LeaguePoints", "Emerald CC", 48),
                new LeagueStandingsRowRecord("Women", "NonAttender Woman", "Turtle CC", 33),
                new LeagueStandingsRowRecord("Under 16s Male", "FirstYear Junior", "Setanta CC", 35),
                new LeagueStandingsRowRecord("Under 16s Male", "NonAttender U16", "Sleepy CC", 41),
                new LeagueStandingsRowRecord("Under 16s Female", "FirstYear WomenJunior", "Setanta CC", 35),
                new LeagueStandingsRowRecord("Under 14s Male", "FirstYear U16", "Young Lads", 60),
                new LeagueStandingsRowRecord("Under 14s Male", "SecondYear U14", "Trailblazers", 42),
                new LeagueStandingsRowRecord("Under 14s Female", "FirstYear WU16", "Young Lasses", 60),
                new LeagueStandingsRowRecord("Under 14s Female", "SecondYear WU14", "Trailblazers", 42),
                new LeagueStandingsRowRecord("Under 12s Male", "NonAttender U12", "Young Lads", 60),
                new LeagueStandingsRowRecord("Under 12s Male", "SecondYear U12", "Turtle CC", 38));

        List<BookingReportRowRecord> signups = List.of(
                new BookingReportRowRecord("Women", "FirstYear WomenJunior", "Setanta CCC"),
                new BookingReportRowRecord("A-Race", "Upgraded from B-Race", "XYZ CC"),
                new BookingReportRowRecord("Women", "WomanWith LeaguePoints", "Emerald CC"),
                new BookingReportRowRecord("A-Race", "ARacerWith LeaguePoints", "Shamrockers"),
                new BookingReportRowRecord("B-Race", "BRacerWith LeaguePoints", "Setanta CC"),
                new BookingReportRowRecord("B-Race", "FirstYear Junior", "Setanta CC"),
                new BookingReportRowRecord("Under 16s Male", "FirstYear U16", "Young Lads"),
                new BookingReportRowRecord("Under 16s Female", "FirstYear WU16", "Young Lads"),
                new BookingReportRowRecord("Under 14s Male", "SecondYear U14", "Trailblazers"),
                new BookingReportRowRecord("Under 14s Female", "SecondYear WU14", "Trailblazers"),
                new BookingReportRowRecord("Under 12s Male", "SecondYear U12", "Turtle CC"));

        List<RiderGriddingPositionRecord> actual = leagueStandingsRepository.findLeaguePositionOfAllUngriddedSignups(leagueStandings, signups, new ArrayList<>());

        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("A-Race", 1, "ARacerWith LeaguePoints", "Shamrockers"),
                new RiderGriddingPositionRecord("B-Race", 1, "BRacerWith LeaguePoints", "Setanta CC"),
                new RiderGriddingPositionRecord("Women", 1, "WomanWith LeaguePoints", "Emerald CC"),
                new RiderGriddingPositionRecord("Under 14s Male", 1, "SecondYear U14", "Trailblazers"),
                new RiderGriddingPositionRecord("Under 14s Female", 1, "SecondYear WU14", "Trailblazers"),
                new RiderGriddingPositionRecord("Under 12s Male", 1, "SecondYear U12", "Turtle CC"));

        assertEquals(expected, actual);
    }
}