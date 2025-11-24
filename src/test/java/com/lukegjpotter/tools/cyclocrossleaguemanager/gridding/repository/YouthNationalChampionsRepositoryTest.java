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
                new RiderGriddingPositionRecord("Under 16s Male", 1, "Caleb Mc Greevy", "VC Glendale"),
                new RiderGriddingPositionRecord("Under 14s Male", 1, "James Cunningham", "Team WORC"),
                new RiderGriddingPositionRecord("Under 16s Female", 1, "Emer Heverin", "All human/VeloRevolution Racing Team"),
                new RiderGriddingPositionRecord("Under 14s Female", 1, "Ava Baker", "Breffni Wheelers"));
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findYouthNationalChampionsWhoAreSignedUp(List.of(
                new BookingReportRowRecord("Under 16s Male", "Caleb Mc Greevy", "VC Glendale"),
                new BookingReportRowRecord("Under 14s Male", "James Cunningham", "Team WORC"),
                new BookingReportRowRecord("Under 16s Female", "Emer Heverin", "All human/VeloRevolution Racing Team"),
                new BookingReportRowRecord("Under 14s Female", "Ava Baker", "Breffni Wheelers")));

        assertEquals(expected, actual);
    }

    @Test
    void findYouthNationalChampionsWhoAreSignedUp_SomeAreRacing() {
        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("Under 16s Male", 1, "Caleb Mc Greevy", "VC Glendale"),
                new RiderGriddingPositionRecord("Under 14s Female", 1, "Ava Baker", "Breffni Wheelers"));
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findYouthNationalChampionsWhoAreSignedUp(List.of(
                new BookingReportRowRecord("A-Race", "John McIntosh", "Round Wheelers"),
                new BookingReportRowRecord("Under 16s Male", "Caleb Mc Greevy", "VC Glendale"),
                new BookingReportRowRecord("Under 14s Female", "Ava Baker", "Breffni Wheelers"),
                new BookingReportRowRecord("B-Race", "Turtle McIntosh", "Shell Wheelers")));

        assertEquals(expected, actual);
    }

    @Test
    void findYouthNationalChampionsWhoAreSignedUp_NoneAreRacing() {
        List<RiderGriddingPositionRecord> expected = new ArrayList<>();
        List<RiderGriddingPositionRecord> actual = youthNationalChampionsRepository.findYouthNationalChampionsWhoAreSignedUp(List.of(
                new BookingReportRowRecord("A-Race", "John Mc Intosh", "Round Wheelers"),
                new BookingReportRowRecord("B-Race", "Turtle Mc Intosh", "Shell Wheelers")));

        assertEquals(expected, actual);
    }
}