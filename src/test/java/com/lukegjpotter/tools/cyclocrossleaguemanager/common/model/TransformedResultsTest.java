package com.lukegjpotter.tools.cyclocrossleaguemanager.common.model;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransformedResultsTest {

    TransformedResults transformedResults;

    @BeforeEach
    void setUp() {
        transformedResults = new TransformedResults();
        transformedResults.setRound("Round 3");
        transformedResults.addResultRow(1, "Ben Dover", "Dover CC", "M40", "Mens B Race", "Male");
        transformedResults.addResultRow(2, "Rog Dover", "Dover CC", "M40", "Mens B Race", "Male");
        transformedResults.addResultRow(1, "Bob Dover", "Dover CC", "M40", "Mens A Race", "Male");
    }

    @Test
    void findResultRowForPositionAndRaceNameAndGender() {
        ResultRow expected = new ResultRow(1, "Ben Dover", "Dover CC", "M40", "Mens B Race", "Male");
        ResultRow actual = transformedResults.findResultRowForPositionAndRaceNameAndGender(1, "Mens B Race", "Male");

        assertEquals(expected, actual);
    }

    @Test
    void findAllResultRowsForRaceNameAndGender() {
        List<ResultRow> expected = Lists.newArrayList(
                new ResultRow(1, "Ben Dover", "Dover CC", "M40", "Mens B Race", "Male"),
                new ResultRow(2, "Rog Dover", "Dover CC", "M40", "Mens B Race", "Male"));
        List<ResultRow> actual = transformedResults.findAllResultRowsForRaceNameAndGender("Mens B Race", "Male");

        assertEquals(expected, actual);
    }
}