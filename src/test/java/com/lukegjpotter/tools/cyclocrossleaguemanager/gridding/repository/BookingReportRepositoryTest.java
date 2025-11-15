package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingReportRepositoryTest {

    @Autowired
    private BookingReportRepository bookingReportRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getDataFromSignUpsGoogleSheet() throws IOException {
        List<BookingReportRowRecord> actual = bookingReportRepository.getDataFromSignUpsGoogleSheet("1yn-ws9qNVrm0H268gLsN0VRaVN5jSqjuKd2VMPCU_uc", false).subList(0, 6);

        List<BookingReportRowRecord> expected = List.of(
                new BookingReportRowRecord("A-Race", "Andy Aracer", "Emerald Wheelers"),
                new BookingReportRowRecord("B-Race", "Billy Bracer", "Shamrock Cycling Club"),
                new BookingReportRowRecord("B-Race Junior", "Johnny Junior", "Celtic Pedalers"),
                new BookingReportRowRecord("Women", "Wanda Wracer", "Gael Riders"),
                new BookingReportRowRecord("Women", "Martha Matherson", "Dublin Spinners"),
                new BookingReportRowRecord("Under 12s Female", "Aoife O'Sullivan", "Claddagh Cyclists"));

        assertEquals(expected, actual);
    }
}