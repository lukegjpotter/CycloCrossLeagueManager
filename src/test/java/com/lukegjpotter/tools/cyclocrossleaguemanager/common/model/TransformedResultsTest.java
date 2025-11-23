package com.lukegjpotter.tools.cyclocrossleaguemanager.common.model;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.ResultRowRecord;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransformedResultsTest {

    TransformedResults transformedResults;

    @BeforeEach
    void setUp() {
        transformedResults = new TransformedResults();
        transformedResults.setRound("Round 3");
        transformedResults.addResultRow("Mens B Race", 1, "Ben Dover", "Dover CC", "M40", "Male");
        transformedResults.addResultRow("Mens B Race", 2, "Rog Dover", "Dover CC", "M40", "Male");
        transformedResults.addResultRow("Mens A Race", 1, "Bob Dover", "Dover CC", "M40", "Male");
    }

    @Test
    void findResultRowForPositionAndRaceNameAndGender() {
        ResultRowRecord expected = new ResultRowRecord("Mens B Race", 1, "Ben Dover", "Dover CC", "M40", "Male");
        ResultRowRecord actual = transformedResults.findResultRowForPositionAndRaceNameAndGender("Mens B Race", 1, "Male");

        assertEquals(expected, actual);
    }

    @Test
    void findAllResultRowsForRaceNameAndGender() {
        List<ResultRowRecord> expected = Lists.newArrayList(
                new ResultRowRecord("Mens B Race", 1, "Ben Dover", "Dover CC", "M40", "Male"),
                new ResultRowRecord("Mens B Race", 2, "Rog Dover", "Dover CC", "M40", "Male"));
        List<ResultRowRecord> actual = transformedResults.findAllResultRowsForRaceNameAndGender("Mens B Race", "Male");

        assertEquals(expected, actual);
    }
}