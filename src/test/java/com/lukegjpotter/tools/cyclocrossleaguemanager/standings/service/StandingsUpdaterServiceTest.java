package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class StandingsUpdaterServiceTest {

    @InjectMocks
    StandingsUpdaterService standingsUpdaterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //@Test
    void load() {
        fail("Not Implemented");
    }
}