package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.testutils.TestUtils;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GriddingControllerTest {

    @Autowired
    TestUtils testUtils;

    @BeforeAll
    static void beforeClass() {
        baseURI = "http://localhost:8080/";
    }

    // ToDo: Comment out this Annotation to enable output monitoring. But remember to manually clean out the file.
    @AfterEach
    void tearDown() {
        testUtils.wipeGriddingSheet("1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec");
    }

    @Test
    void gridRace() throws IOException {
        given()
                .contentType(ContentType.JSON)
                .body(new GriddingRequestRecord("https://docs.google.com/spreadsheets/d/1tQCo5fMjuTVzHUkKvJO9T5BMu9n_Pb0dGgu0Rz-zVpE/", "https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/", 2))
                .when()
                .post("/gridding")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("gridding", equalTo("https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/"),
                        "errorMessage", emptyString());

        String griddingGoogleSheetId = "1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec";
        String actualRaceGridding = testUtils.aRaceGriddingToString(griddingGoogleSheetId)
                + testUtils.womensRaceGriddingToString(griddingGoogleSheetId)
                + testUtils.bRaceGriddingToString(griddingGoogleSheetId)
                + testUtils.u16BoysRaceGriddingToString(griddingGoogleSheetId)
                + testUtils.u16GirlsRaceGriddingToString(griddingGoogleSheetId)
                + testUtils.u14BoysRaceGriddingToString(griddingGoogleSheetId)
                + testUtils.u14GirlsRaceGriddingToString(griddingGoogleSheetId)
                + testUtils.u12BoysRaceGriddingToString(griddingGoogleSheetId)
                + testUtils.u12GirlsRaceGriddingToString(griddingGoogleSheetId);

        String expectedRaceGridding = """
                Tadhg Killeen, Kilcullen Cycling Club Murphy Geospacial
                Ronan O'Flynn, Orwell Wheelers Cycling Club
                Francis Yates, Un-Attached Leinster
                John Jordan, Un-Attached Leinster
                Esther Wong, Shipden Apex
                Hannah McClorey, Hess CT
                Doireann Killeen, Kilcullen Cycling Club Murphy Geospacial
                Grace O'Rourke, Spellman-Dublin Port
                Deirdre O Toole, VC Beechwood
                John Slater, Castleknock Cycling Club
                Bob Maye, Clonard RC
                Matthew Boughton, Lucan Cycling Road Club
                Tommy Buckley, Naomh Barrog Cycling Club
                Matt Lynch, Orwell Wheelers Cycling Club
                Emer Heverin, All human/VeloRevolution Racing Team
                Katie Turner, Orwell Wheelers Cycling Club
                J-Jay O'Brien, Bohermeen CC
                Kealan Doherty, Foyle CC
                Ciara O'Connor, Drogheda Wheelers
                Laoise Crinion, IMBRC
                Benjamin Cunningham, Orwell Wheelers Cycling Club
                Logan McAreavey, Bellurgan Wheelers
                Amelia Finnegan, Square Wheels
                """;

        assertEquals(expectedRaceGridding, actualRaceGridding);
    }
}