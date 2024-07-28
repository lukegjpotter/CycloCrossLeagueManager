package com.lukegjpotter.tools.cyclocrossleaguemanager.results.service;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class ResultsExtractorServiceTest {

    @InjectMocks
    ResultsExtractorService resultsExtractorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //@Test
    void extract() {
        String expected = "";
        String actual = resultsExtractorService.extract("").getRound();

        //assertEquals(expected, actual, "Expected: " + expected + ", but got: " + actual);
        fail("Not Implemented");
    }
}