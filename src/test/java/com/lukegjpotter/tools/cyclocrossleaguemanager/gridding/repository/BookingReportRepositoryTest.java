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
        List<BookingReportRowRecord> actual = bookingReportRepository.getDataFromSignUpsGoogleSheet("1tQCo5fMjuTVzHUkKvJO9T5BMu9n_Pb0dGgu0Rz-zVpE", false).subList(0, 5);

        List<BookingReportRowRecord> expected = List.of(
                new BookingReportRowRecord("A race", "Andy Aracer", "Sponsored Team"),
                new BookingReportRowRecord("B race", "Billy Bracer", "Shamrock Cycling Club"),
                new BookingReportRowRecord("B race Junior", "Johnny Junior", "Celtic Pedalers"),
                new BookingReportRowRecord("Women", "Wanda Wracer", "Gael Riders"),
                new BookingReportRowRecord("Women", "Martha Matherson", "Dublin Spinners"));

        assertEquals(expected, actual);
    }
}