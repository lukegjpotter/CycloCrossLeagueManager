package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.testutils.GriddingTestMocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingReportRepositoryTest {

    @Autowired
    private BookingReportRepository bookingReportRepository;
    @Autowired
    private GriddingTestMocks griddingTestMocks;

    @MockitoBean
    private GoogleSheetsService googleSheetsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getDataFromSignUpsGoogleSheet() throws IOException {
        when(googleSheetsService.getSpreadsheetHeaders("1yn-ws9qNVrm0H268gLsN0VRaVN5jSqjuKd2VMPCU_uc", "Report")).thenReturn(griddingTestMocks.getBookingReportHeaders());
        when(googleSheetsService.readSpreadsheetValuesInRange("1yn-ws9qNVrm0H268gLsN0VRaVN5jSqjuKd2VMPCU_uc", "Report!F2:AB")).thenReturn(griddingTestMocks.getBookingReport2Values());

        List<BookingReportRowRecord> actual = bookingReportRepository.getDataFromSignUpsGoogleSheet("1yn-ws9qNVrm0H268gLsN0VRaVN5jSqjuKd2VMPCU_uc", false).subList(0, 6);

        List<BookingReportRowRecord> expected = List.of(
                new BookingReportRowRecord("A-Race", "Andy Aracer", "Emerald Wheelers"),
                new BookingReportRowRecord("B-Race", "Billy Bracer", "Shamrock Cycling Club"),
                new BookingReportRowRecord("B-Race", "Johnny Junior", "Celtic Pedalers"),
                new BookingReportRowRecord("Women", "Wanda Wracer", "Gael Riders"),
                new BookingReportRowRecord("Women", "Martha Matherson", "Dublin Spinners"),
                new BookingReportRowRecord("Under 12s Female", "Aoife O'Sullivan", "Claddagh Cyclists"));

        assertEquals(expected, actual);
    }

    @Test
    void getDataFromSignUpsGoogleSheet_VariousTicketTypeSpellings() throws IOException {
        List<BookingReportRowRecord> actual = bookingReportRepository.getDataFromSignUpsGoogleSheet("1GnQEL55ZMzOsxLJZodqGRygDLNpT-Q6NdCGEX1vjP0k", false);

        List<BookingReportRowRecord> expected = List.of(
                new BookingReportRowRecord("Women", "Rhiannon Dolan", "TC Racing"),
                new BookingReportRowRecord("A-Race", "Sean O Leary", "Lucan Cycling Road Club"),
                new BookingReportRowRecord("B-Race", "Luke Kehoe", "Lucan Cycling Road Club"),
                new BookingReportRowRecord("A-Race", "Peter Boaden", "Gorey Cycling Club"),
                new BookingReportRowRecord("A-Race", "William Brown", "Un-Attached"),
                new BookingReportRowRecord("Under 16s Male", "James Cunningham", "Pinergy Orwell Wheelers"),
                new BookingReportRowRecord("Under 14s Male", "Jake Govan", "Pinergy Orwell Wheelers"),
                new BookingReportRowRecord("Under 12s Male", "Nathan Baker", "Breffni Wheelers"));

        assertEquals(expected, actual);
    }
}