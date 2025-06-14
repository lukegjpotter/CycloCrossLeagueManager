package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.BookingReportRepository;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.GriddingRepository;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.LeagueStandingsRepository;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.UciPointsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class GriddingServiceTest {

    @InjectMocks
    GriddingService griddingService;
    @Mock
    LeagueStandingsRepository leagueStandingsRepository;
    @Mock
    UciPointsRepository uciPointsRepository;
    @Mock
    BookingReportRepository bookingReportRepository;
    @Mock
    GriddingRepository griddingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //@Test todo implement Gridding Service Test
    void gridSignups() {
        fail("Not Implemented");
    }
}