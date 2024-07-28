package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderUciPointRecord;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class UciPointsRepositoryTest {

    @InjectMocks
    UciPointsRepository uciPointsRepository;
    @Mock
    List<RiderUciPointRecord> ridersWithUciPoints;
    @Mock
    BookingReportRepository bookingReportRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //@Test
    void findAll() {
        fail("Not Implemented");
    }

    //@Test
    void extractSignUps() {
        fail("Not Implemented");
    }
}