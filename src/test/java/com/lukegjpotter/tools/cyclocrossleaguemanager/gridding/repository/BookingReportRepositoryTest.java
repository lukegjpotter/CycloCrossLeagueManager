package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.service.GoogleSheetsService;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.testutils.GriddingTestMocks;
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
        when(googleSheetsService.getSpreadsheetHeaders("1yn-ws9qNVrm0H268gLsN0VRaVN5jSqjuKd2VMPCU_uc", "Report")).thenReturn(griddingTestMocks.getBookingReport2024Headers());
        when(googleSheetsService.readSpreadsheetValuesInRange("1yn-ws9qNVrm0H268gLsN0VRaVN5jSqjuKd2VMPCU_uc", "Report!F2:AB")).thenReturn(griddingTestMocks.getBookingReport2024Values());

        List<BookingReportRowRecord> actual = bookingReportRepository.getDataFromSignUpsGoogleSheet("1yn-ws9qNVrm0H268gLsN0VRaVN5jSqjuKd2VMPCU_uc", false).subList(0, 6);

        List<BookingReportRowRecord> expected = List.of(
                new BookingReportRowRecord("A-Race", "Andy Aracer", "Emerald Wheelers"),
                new BookingReportRowRecord("B-Race", "Billy Bracer", "Shamrock Cycling Club"),
                new BookingReportRowRecord("B-Race", "Johnny Junior", "Celtic Pedalers"),
                new BookingReportRowRecord("Women", "Wanda Wracer", "Gael Riders"),
                new BookingReportRowRecord("Women", "Martha Matherson", "Dublin Spinners"),
                new BookingReportRowRecord("Under 12s Female", "Aoife O Sullivan", "Claddagh Cyclists"));

        assertEquals(expected, actual);
    }

    @Test
    void getDataFromSignUpsGoogleSheet_VariousTicketTypeSpellings() throws IOException {
        when(googleSheetsService.getSpreadsheetHeaders("1GnQEL55ZMzOsxLJZodqGRygDLNpT-Q6NdCGEX1vjP0k", "Report")).thenReturn(griddingTestMocks.getBookingReport2025Headers());
        when(googleSheetsService.readSpreadsheetValuesInRange("1GnQEL55ZMzOsxLJZodqGRygDLNpT-Q6NdCGEX1vjP0k", "Report!H2:AA")).thenReturn(griddingTestMocks.getBookingReport2025Values());

        List<BookingReportRowRecord> actual = bookingReportRepository.getDataFromSignUpsGoogleSheet("1GnQEL55ZMzOsxLJZodqGRygDLNpT-Q6NdCGEX1vjP0k", false);

        List<BookingReportRowRecord> expected = List.of(
                new BookingReportRowRecord("Women", "Sarah Mc Pedaler", "CT Racing"),
                new BookingReportRowRecord("A-Race", "Sean O Chainring", "West Dublin Racing"),
                new BookingReportRowRecord("B-Race", "Luke Bottom-Bracket", "West Dublin Racing"),
                new BookingReportRowRecord("A-Race", "Peter Peterson", "Wicklow Pedalers"),
                new BookingReportRowRecord("A-Race", "Billy Brown", "Un-Attached"),
                new BookingReportRowRecord("Under 16s Male", "James Jameson", "South-Dublin Wheelers"),
                new BookingReportRowRecord("Under 14s Male", "Jake Jakeson", "South-Dublin Wheelers"),
                new BookingReportRowRecord("Under 12s Male", "Nathan Fillion", "Castle Wheelers"));

        assertEquals(expected, actual);
    }
}