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
class UciPointsRepositoryTest {

    @Autowired
    UciPointsRepository uciPointsRepository;

    @Test
    void findRidersWithUciPointsWhoAreSignedUp_NoneAreSignedUp() {
        List<RiderGriddingPositionRecord> actual = uciPointsRepository.findRidersWithUciPointsWhoAreSignedUp(List.of(
                new BookingReportRowRecord("A-Race", "John Johnson", "Trinity Racing"),
                new BookingReportRowRecord("A-Race", "Kevin Kevinson", "Inspired Cycling"),
                new BookingReportRowRecord("A-Race", "Darnell O'Moore", "McCovey Cycles"),
                new BookingReportRowRecord("Women", "Aliyah Robertson", "Tifosi"),
                new BookingReportRowRecord("Women", "Roisin Carey", "Loughbourgh Lightning")));

        List<RiderGriddingPositionRecord> expected = new ArrayList<>();

        assertEquals(expected, actual);
    }

    @Test
    void findRidersWithUciPointsWhoAreSignedUp_FiveAreSignedUp() {
        List<RiderGriddingPositionRecord> actual = uciPointsRepository.findRidersWithUciPointsWhoAreSignedUp(List.of(
                new BookingReportRowRecord("A-Race", "Dean Harvey", "Trinity Racing"),
                new BookingReportRowRecord("A-Race", "Kevin McCambridge", "Inspired Cycling"),
                new BookingReportRowRecord("A-Race", "Darnell Moore", "McCovey Cycles"),
                new BookingReportRowRecord("Women", "Aliyah Rafferty", "Tifosi"),
                new BookingReportRowRecord("Women", "Roisin Lally", "Loughbourgh Lightning")));

        List<RiderGriddingPositionRecord> expected = List.of(
                new RiderGriddingPositionRecord("A-Race", 1, "Dean Harvey", ""),
                new RiderGriddingPositionRecord("A-Race", 2, "Kevin McCambridge", ""),
                new RiderGriddingPositionRecord("A-Race", 3, "Darnell Moore", ""),
                new RiderGriddingPositionRecord("Women", 1, "Aliyah Rafferty", ""),
                new RiderGriddingPositionRecord("Women", 2, "Roisin Lally", ""));

        assertEquals(expected, actual);
    }
}