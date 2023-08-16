package common.controller;

import common.service.ResultsAndStandingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String urlInput = "";
        Mockito.when(resultsAndStandingsService.performETL(urlInput)).thenReturn("");

        String expected = "";
        String actual = resultsAndStandingsController.raceResults(urlInput);

        assertEquals(expected, actual);
    }
}