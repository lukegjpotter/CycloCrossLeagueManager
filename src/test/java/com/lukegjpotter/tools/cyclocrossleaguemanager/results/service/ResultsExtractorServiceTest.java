package com.lukegjpotter.tools.cyclocrossleaguemanager.results.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultsExtractorServiceTest {

    @InjectMocks
    ResultsExtractorService resultsExtractorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void extract() {
        String expected = "";
        String actual = resultsExtractorService.extract("").getRound();

        assertEquals(expected, actual, "Expected: " + expected + ", but got: " + actual);
    }
}