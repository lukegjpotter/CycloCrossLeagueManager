package com.lukegjpotter.tools.cyclocrossleaguemanager.testutils;

import com.google.api.services.sheets.v4.model.ValueRange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GriddingTestMocks {

    private final static Logger logger = LoggerFactory.getLogger(GriddingTestMocks.class);

    public List<String> getBookingReport2024Headers() {
        logger.trace("Getting Mocked Booking Report 2024 Headers.");

        return List.of(
                "Event Name", "Order Timestamp", "Status", "Order ID", "Barcode", "TicketType", "First Name",
                "Last Name", "Email", "Verify Email", "Gender", "Date of Birth", "Age", "County", "Mobile",
                "Country Name", "Country", "Vehicle Make/Model", "Vehicle Registration", "Vehicle Role",
                "Emergency Contact Name", "Emergency Contact Phone", "Public health measures for COVID-19",
                "I have read, understand, and agree to abide by the above Public health measures for COVID-19.",
                "Race Waiver", "I accept race waiver", "Cycling Ireland Licence Number", "CI Club", "Rider Category",
                "UCI ID", "Nationality", "Team", "I agree to be contacted by Eventmaster",
                "I agree to be contacted by this organiser", "CI Licence Number", "CI Club", "CI mid",
                "CI Rider Category", "CI Women Ranking", "Payment Type", "Selling Mode", "Ticket Price", "Ticket Fee",
                "Discount", "Discount Amount", "Discount Code Used", "Total Price Paid Per Ticket", "Timeslot Name",
                "Timeslot Date", "Booking Date", "Booking Month", "Race Number", "Access Code", "Comment",
                "Additional Purchases", "Additional Purchases Details", "Additional Purchases Options",
                "Additional Purchases Variants", "Additional Purchases Total Paid"
        );
    }

    public List<String> getBookingReport2025Headers() {
        logger.trace("Getting Mocked Booking Report 2025 Headers.");

        return List.of(
                "Event Name", "Order Timestamp", "Status", "Order ID", "Barcode", "Timeslot Date",
                "Timeslot Name", "TicketType", "First Name", "Last Name", "Email", "Verify Email", "Gender",
                "Date of Birth", "Age", "Mobile", "Vehicle Make/Model", "Vehicle Registration", "Vehicle Role",
                "Emergency Contact Name", "Emergency Contact Phone", "Event Waiver", "I accept event waiver",
                "I agree to be contacted by Eventmaster", "I agree to be contacted by this organiser",
                "CI Licence Number", "CI Club", "CI mid", "CI Rider Category", "CI Women Ranking", "UCI ID",
                "Payment Type", "Selling Mode", "Ticket Price", "Ticket Fee", "Discount", "Discount Amount",
                "Discount Code Used", "Total Price Paid Per Ticket", "Booking Date", "Booking Month", "Race Number",
                "Access Code", "Comment", "Additional Purchases", "Additional Purchases Details",
                "Additional Purchases Options", "Additional Purchases Variants", "Additional Purchases Total Paid"
        );
    }

    public ValueRange getBookingReport2024Values() {

        logger.trace("Getting Mocked Booking Report 2024 Values.");

        return new ValueRange().setValues(
                List.of(
                        List.of("A race", "Andy", "Aracer", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Dublin 1", "00353-87-1111111", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40069", "Emerald Wheelers"),
                        List.of("B race", "Billy", "Bracer", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Dublin 2", "00353-87-1111112", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40070", "Shamrock Cycling Club"),
                        List.of("B race Junior", "Johnny", "Junior", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Dublin 3", "00353-87-1111113", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40071", "Celtic Pedalers"),
                        List.of("Women", "Wanda", "Wracer", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Dublin 6W", "00353-87-1111114", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40072", "Gael Riders"),
                        List.of("Women", "Martha", "Matherson", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Meath", "00353-87-1111115", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40073", "Dublin Spinners"),
                        List.of("Under 12s", "Aoife", "O'Sullivan", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Meath", "00353-87-1111116", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40074", "Claddagh Cyclists"),
                        List.of("B race", "Liam", "Murphy", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Meath", "00353-87-1111117", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40075", "Limerick Rollers"),
                        List.of("Under 12s", "Niamh", "Gallagher", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Kildare", "00353-87-1111118", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40076", "Galway Roadsters"),
                        List.of("A race", "Conor", "Byrne", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Kildare", "00353-87-1111119", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40077", "Belfast Bikers"),
                        List.of("Women Junior", "Siobhán", "O'Reilly", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Kildare", "00353-87-1111120", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40078", "Kerry Cruisers"),
                        List.of("A race", "Rory", "McDonagh", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Kildare", "00353-87-1111121", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40079", "Emerald Wheelers"),
                        List.of("Women", "Caoimhe", "Brennan", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Cavan", "00353-87-1111122", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40080", "Shamrock Cycling Club"),
                        List.of("Under 12s", "Eoin", "Fitzpatrick", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Meath", "00353-87-1111123", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40081", "Celtic Pedalers"),
                        List.of("Under 14s", "Orlaith", "Kelly", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Louth", "00353-87-1111124", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40082", "Gael Riders"),
                        List.of("B race", "Cian", "Doyle", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Monaghan", "00353-87-1111125", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40083", "Dublin Spinners"),
                        List.of("Under 10s", "Fiona", "McCarthy", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Louth", "00353-87-1111126", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40084", "Claddagh Cyclists"),
                        List.of("A race", "Seán", "Kavanagh", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Louth", "00353-87-1111127", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40085", "Limerick Rollers"),
                        List.of("Women", "Aisling", "Keane", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Louth", "00353-87-1111128", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40086", "Galway Roadsters"),
                        List.of("Under 14s", "Patrick", "Duffy", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Meath", "00353-87-1111129", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40087", "Belfast Bikers"),
                        List.of("Under 16s", "Maeve", "Nolan", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Wexford", "00353-87-1111130", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40088", "Kerry Cruisers"),
                        List.of("Under 12s", "Declan", "O'Connor", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Kildare", "00353-87-1111131", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40089", "Emerald Wheelers"),
                        List.of("Under 14s", "Gráinne", "Quinn", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Kildare", "00353-87-1111132", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40090", "Shamrock Cycling Club"),
                        List.of("B race Junior", "Darragh", "O'Malley", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Dublin", "00353-87-1111133", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40091", "Celtic Pedalers"),
                        List.of("Under 16s", "Bríd", "Flynn", "email@email.com", "email@email.com", "Female", "04.10.1974", "50", "Dublin", "00353-87-1111134", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40092", "Gael Riders"),
                        List.of("Under 14s", "Eoghan", "Ryan", "email@email.com", "email@email.com", "Male", "04.10.1974", "50", "Dublin 3", "00353-87-1111135", "", "", "", "", "", "Dee Znuts", "872222222", "", "", "", "yes", "24A40093", "Dublin Spinners")
                )
        );
    }

    public ValueRange getBookingReport2025Values() {

        logger.trace("Getting Mocked Booking Report 2025 Values.");

        return new ValueRange().setValues(
                List.of(
                        List.of("Junior Women / U23 women", "Rhiannon", "Dolan", "email@email.com", "email@email.com", "Female", "01.01.2008", "17", "", "", "", "", "", "", "", "yes", "yes", "yes", "", "TC Racing"),
                        List.of("Junior A race / U23", "Sean", "O Leary", "email@email.com", "email@email.com", "Male", "02.01.2008", "17", "", "", "", "", "", "", "", "yes", "no", "yes", "", "Lucan Cycling Road Club"),
                        List.of("Junior B race / U23", "Luke", "Kehoe", "email@email.com", "email@email.com", "Male", "03.01.2008", "17", "", "", "", "", "", "", "", "yes", "yes", "no", "", "Lucan Cycling Road Club"),
                        List.of("A race M60", "Peter", "Boaden", "email@email.com", "email@email.com", "Male", "04.01.2008", "17", "", "", "", "", "", "", "", "yes", "no", "no", "", "Gorey Cycling Club"),
                        List.of("A race (non licence)", "William", "Brown", "email@email.com", "email@email.com", "Male", "05.01.2008", "17", "", "", "", "", "", "", "", "yes", "no", "no", ""),
                        List.of("Under 16s", "James", "Cunningham", "email@email.com", "email@email.com", "Male", "06.01.2008", "17", "", "", "", "", "", "", "", "yes", "no", "no", "", "Pinergy Orwell Wheelers"),
                        List.of("Under 14s", "Jake", "Govan", "email@email.com", "email@email.com", "Male", "07.01.2008", "17", "", "", "", "", "", "", "", "yes", "no", "no", "", "Pinergy Orwell Wheelers"),
                        List.of("Youth U12 (2014-2015) (non-licence)", "Nathan", "Baker", "email@email.com", "email@email.com", "Male", "08.01.2008", "17", "", "", "", "", "", "", "", "yes", "no", "yes", "", "Breffni Wheelers")
                )
        );
    }
}
