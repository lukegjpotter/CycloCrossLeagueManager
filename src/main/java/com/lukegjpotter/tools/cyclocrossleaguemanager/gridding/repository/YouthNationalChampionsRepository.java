package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.BookingReportRowRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class YouthNationalChampionsRepository {

    public List<RiderGriddingPositionRecord> findYouthNationalChampionsWhoAreSignedUp(final List<BookingReportRowRecord> signupsBookingReportList) {

        final List<RiderGriddingPositionRecord> youthNationalChampions = new ArrayList<>();

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader("src/main/resources/YouthNationalChampions.csv")).withSkipLines(1).build()) {
            String[] lineInArray;

            while ((lineInArray = csvReader.readNext()) != null) {
                RiderGriddingPositionRecord youthNationalChampion = new RiderGriddingPositionRecord(lineInArray[0].trim(), 1, lineInArray[1].trim(), lineInArray[2].trim());
                if (signupsBookingReportList.contains(new BookingReportRowRecord(youthNationalChampion.raceCategory(), youthNationalChampion.fullName(), youthNationalChampion.clubName())))
                    youthNationalChampions.add(youthNationalChampion);
            }
        } catch (IOException | CsvValidationException e) {
            return youthNationalChampions;
        }

        return youthNationalChampions;
    }
}
