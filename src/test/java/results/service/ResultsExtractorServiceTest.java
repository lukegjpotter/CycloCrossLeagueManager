package results.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultsExtractorServiceTest {

    @Autowired
    ResultsExtractorService resultsExtractorService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void extract() {
        String expected = "Round 3";
        String actual = resultsExtractorService.extract("").getRound();

        assertEquals(expected, actual, "Expected: " + expected + ", but got: " + actual);
    }
}