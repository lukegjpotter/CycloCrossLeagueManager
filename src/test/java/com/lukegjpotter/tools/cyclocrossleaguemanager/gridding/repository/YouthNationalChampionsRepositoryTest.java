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
    void findYouthNationalChampionsWhoAreSignedUp_AllAreRacing() {
        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("Boy's U16", 1, "Caleb McGreevy", "VC Glendale"),
                new RiderGriddingPositionRecord("Boy's U14", 1, "James Cunningham", "Team WORC"),
                new RiderGriddingPositionRecord("Girl's U16", 1, "Emer Heverin", "All human/VeloRevolution Racing Team"),
                new RiderGriddingPositionRecord("Girl's U14", 1, "Ava Baker", "Breffni Wheelers"));
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findYouthNationalChampionsWhoAreSignedUp(List.of(
                new BookingReportRowRecord("Boy's U16", "Caleb McGreevy", "VC Glendale"),
                new BookingReportRowRecord("Boy's U14", "James Cunningham", "Team WORC"),
                new BookingReportRowRecord("Girl's U16", "Emer Heverin", "All human/VeloRevolution Racing Team"),
                new BookingReportRowRecord("Girl's U14", "Ava Baker", "Breffni Wheelers")));

        assertEquals(expected, actual);
    }

    @Test
    void findYouthNationalChampionsWhoAreSignedUp_SomeAreRacing() {
        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("Boy's U16", 1, "Caleb McGreevy", "VC Glendale"),
                new RiderGriddingPositionRecord("Girl's U14", 1, "Ava Baker", "Breffni Wheelers"));
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findYouthNationalChampionsWhoAreSignedUp(List.of(
                new BookingReportRowRecord("A-Race", "John McIntosh", "Round Wheelers"),
                new BookingReportRowRecord("Boy's U16", "Caleb McGreevy", "VC Glendale"),
                new BookingReportRowRecord("Girl's U14", "Ava Baker", "Breffni Wheelers"),
                new BookingReportRowRecord("B-Race", "Turtle McIntosh", "Shell Wheelers")));

        assertEquals(expected, actual);
    }

    @Test
    void findYouthNationalChampionsWhoAreSignedUp_NoneAreRacing() {
        List<RiderGriddingPositionRecord> expected = new ArrayList<>();
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findYouthNationalChampionsWhoAreSignedUp(List.of(
                new BookingReportRowRecord("A-Race", "John McIntosh", "Round Wheelers"),
                new BookingReportRowRecord("B-Race", "Turtle McIntosh", "Shell Wheelers")));

        assertEquals(expected, actual);
    }
}