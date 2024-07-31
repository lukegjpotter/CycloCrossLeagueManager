package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.LeagueStandingsRowRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
        LeagueStandingsRowRecord expected = new LeagueStandingsRowRecord("A-Race", "Javan Nulty", "Un-Attached Leinster", 268);

        assertEquals(expected, actual);
    }

    @Test
    void loadDataFromLeagueStandingsGoogleSheet_LastYear_TopTotal_NotAdjustedTotal() throws IOException {
        LeagueStandingsRowRecord actual = leagueStandingsRepository.loadDataFromLeagueStandingsGoogleSheet(1).get(0);
        LeagueStandingsRowRecord expected = new LeagueStandingsRowRecord("A-Race", "Ronan Killeen", "Lucan Cycling Road Club", 255);

        assertEquals(expected, actual);
    }

    //@Test
    void findLeaguePositionOfAllUngriddedSignups() {
        fail("Not Implemented");
    }
}