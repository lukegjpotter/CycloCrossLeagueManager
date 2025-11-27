package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.RangeAndMinimumIndexRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.exception.GriddingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GoogleSheetsRangeBuilderServiceTest {

    @Autowired
    GoogleSheetsRangeBuilderService googleSheetsRangeBuilderService;

    @Test
    void buildGoogleSheetsRange_positiveIndicesWithSheetName() {
        RangeAndMinimumIndexRecord actual = googleSheetsRangeBuilderService.buildGoogleSheetsRange("Report", Stream.of(5, 4, 3, 2, 1));
        RangeAndMinimumIndexRecord expected = new RangeAndMinimumIndexRecord("Report!B2:F", 1);

        assertEquals(expected, actual);
    }

    @Test
    void buildGoogleSheetsRange_negitiveIndicesWithSheetName() {
        assertThrows(GriddingException.class, () -> googleSheetsRangeBuilderService.buildGoogleSheetsRange("Report", Stream.of(-1, -1, -1, -1, -1)));
    }

    @Test
    void buildGoogleSheetsRange_mixPositiveNegativeIndicesWithoutSheetName() {
        RangeAndMinimumIndexRecord actual = googleSheetsRangeBuilderService.buildGoogleSheetsRange("", Stream.of(5, 4, 3, 2, 1, -1));
        RangeAndMinimumIndexRecord expected = new RangeAndMinimumIndexRecord("!B2:F", 1);

        assertEquals(expected, actual);
    }
}