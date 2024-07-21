package com.lukegjpotter.tools.cyclocrossleaguemanager.common.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.ResultsAndStandingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ResultsAndStandingsControllerTest {

    @InjectMocks
    ResultsAndStandingsController resultsAndStandingsController;
    @Mock
    ResultsAndStandingsService resultsAndStandingsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void raceResults() {
        // ToDo: Replace with RestAssured.

        String urlInput = "";
        Mockito.when(resultsAndStandingsService.performETL(urlInput)).thenReturn("");

        String expected = "";
        String actual = resultsAndStandingsController.raceResults(urlInput);

        assertEquals(expected, actual);
    }
}