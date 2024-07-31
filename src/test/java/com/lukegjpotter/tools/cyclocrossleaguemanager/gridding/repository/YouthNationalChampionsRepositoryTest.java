package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class YouthNationalChampionsRepositoryTest {

    @Autowired
    YouthNationalChampionsRepository youthNationalChampionsRepository;

    @Test
    void findAll_AllAreRacing() {
        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("Boy's U16", 1, "Curtis McKee", "Spellman-Dublin Port"),
                new RiderGriddingPositionRecord("Boy's U14", 1, "James Cunningham", "Orwell Wheelers Cycling Club"),
                new RiderGriddingPositionRecord("Girl's U16", 1, "Emer Heverin", "Kinning Cycles Cycling Club"),
                new RiderGriddingPositionRecord("Girl's U14", 1, "Katie Turner", "Orwell Wheelers Cycling Club"));
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findAll(new ArrayList<>(List.of(
                new BookingReportRowRecord("Boy's U16", "Curtis McKee", "Spellman-Dublin Port"),
                new BookingReportRowRecord("Boy's U14", "James Cunningham", "Orwell Wheelers Cycling Club"),
                new BookingReportRowRecord("Girl's U16", "Emer Heverin", "Kinning Cycles Cycling Club"),
                new BookingReportRowRecord("Girl's U14", "Katie Turner", "Orwell Wheelers Cycling Club"))));

        assertEquals(expected, actual);
    }

    @Test
    void findAll_SomeAreRacing() {
        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("Boy's U16", 1, "Curtis McKee", "Spellman-Dublin Port"),
                new RiderGriddingPositionRecord("Girl's U14", 1, "Katie Turner", "Orwell Wheelers Cycling Club"));
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findAll(new ArrayList<>(List.of(
                new BookingReportRowRecord("A-Race", "John McIntosh", "Round Wheelers"),
                new BookingReportRowRecord("Boy's U16", "Curtis McKee", "Spellman-Dublin Port"),
                new BookingReportRowRecord("Girl's U14", "Katie Turner", "Orwell Wheelers Cycling Club"),
                new BookingReportRowRecord("B-Race", "Turtle McIntosh", "Shell Wheelers"))));

        assertEquals(expected, actual);
    }

    @Test
    void findAll_NoneAreRacing() {
        List<RiderGriddingPositionRecord> expected = new ArrayList<>();
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findAll(new ArrayList<>(List.of(
                new BookingReportRowRecord("A-Race", "John McIntosh", "Round Wheelers"),
                new BookingReportRowRecord("B-Race", "Turtle McIntosh", "Shell Wheelers"))));

        assertEquals(expected, actual);
    }
}