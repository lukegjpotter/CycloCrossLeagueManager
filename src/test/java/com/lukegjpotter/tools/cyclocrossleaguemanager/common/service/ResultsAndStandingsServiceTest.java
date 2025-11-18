package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.TransformedResults;
import com.lukegjpotter.tools.cyclocrossleaguemanager.results.service.ResultsExtractorService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.service.StandingsUpdaterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ResultsAndStandingsServiceTest {

    @InjectMocks
    ResultsAndStandingsService resultsAndStandingsService;
    @Mock
    ResultsExtractorService resultsExtractorService;
    @Mock
    StandingsUpdaterService standingsUpdaterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void performETL() {
        String resultsUrl = "";
        TransformedResults transformedResults = new TransformedResults();
        Mockito.when(resultsExtractorService.extract(resultsUrl)).thenReturn(transformedResults);
        //Mockito.doNothing().when(standingsUpdaterService).load(transformedResults);

        String expected = "";
        String actual = resultsAndStandingsService.performETL(resultsUrl);

        assertEquals(expected, actual);
    }
}