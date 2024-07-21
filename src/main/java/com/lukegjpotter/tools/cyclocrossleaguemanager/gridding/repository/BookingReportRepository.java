package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingReportRepository {

    private final List<BookingReportRowRecord> ridersSignedUp = List.of();
    @Value("${googlesheets.apikey}")
    private String googleSheetsApiKey;

    public BookingReportRepository() {
    }

    public List<BookingReportRowRecord> findAll() {
        if (!ridersSignedUp.isEmpty()) return ridersSignedUp;

        return new ArrayList<>();
    }

    public void deleteUciRiders(ArrayList<BookingReportRowRecord> bookingReportRowRecords) {
    }

    public void loadDataFromSignUpsGoogleSheet(String signUpsGoogleSheet) {
        // ToDo: Load the data from the Booking Report Excel file.
    }
}
