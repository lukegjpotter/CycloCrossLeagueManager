package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.ResultRowRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ResultsRepositoryTest {

    @Autowired
    ResultsRepository resultsRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getResultRowsFromResultsGoogleSheet() {
        List<ResultRowRecord> actual = new ArrayList<>(resultsRepository.getResultRowsFromResultsGoogleSheet("1CUxbgIU_gEIu3-ZKV0OD0nNM8TrvFZewf5PbXlf2fsA").values().stream().toList());
        actual.sort(Comparator
                .comparing(ResultRowRecord::raceCategory)
                .thenComparingInt(ResultRowRecord::position)
                .thenComparing(ResultRowRecord::ageCategory));
        actual = actual.subList(0, 5);

        List<ResultRowRecord> expected = List.of(
                new ResultRowRecord("A-Race", 1, "Conor Regan", "Kilcullen Cycling Club Murphy Geospacial", "Junior", ""),
                new ResultRowRecord("A-Race", 2, "Sean Lundy", "UCD Cycling Club", "Senior", ""),
                new ResultRowRecord("A-Race", 3, "Kevin Keane", "St. Tiernan's Cycling Club", "M40", ""),
                new ResultRowRecord("A-Race", 4, "Sean Nolan", "Navan Road Club", "Senior", ""),
                new ResultRowRecord("A-Race", 5, "Darragh Mc Carter", "Spellman-Dublin Port", "M40", ""));

        assertEquals(expected, actual);
    }
}