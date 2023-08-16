package common.service;

import common.model.TransformedResults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import results.service.ResultsExtractorService;
import standings.service.StandingsLoaderService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultsAndStandingsServiceTest {

    @InjectMocks
    ResultsAndStandingsService resultsAndStandingsService;
    @Mock
    ResultsExtractorService resultsExtractorService;
    @Mock
    StandingsLoaderService standingsLoaderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void performETL() {
        String resultsUrl = "";
        TransformedResults transformedResults = new TransformedResults();
        Mockito.when(resultsExtractorService.extract(resultsUrl)).thenReturn(transformedResults);
        Mockito.doNothing().when(standingsLoaderService).load(transformedResults);

        String expected = "";
        String actual = resultsAndStandingsService.performETL(resultsUrl);

        assertEquals(expected, actual);
    }
}