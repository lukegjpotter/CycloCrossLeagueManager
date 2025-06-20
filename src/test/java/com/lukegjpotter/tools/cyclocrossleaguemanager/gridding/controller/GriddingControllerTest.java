package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.model.RiderGriddingPositionRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.GriddingRepository;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GriddingControllerTest {

    @Autowired
    GriddingRepository griddingRepository;

    @BeforeAll
    static void beforeClass() {
        baseURI = "http://localhost:8080/";
    }

    // ToDo: Comment out this Annotation to enable output monitoring. But remember to manually clean out the file.
    @AfterEach
    void tearDown() {
        griddingRepository.writeGriddingToGoogleSheet(
                new ArrayList<>(List.of(new RiderGriddingPositionRecord("A-Race", 1, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 2, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 3, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 4, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 5, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 6, "", ""),
                        new RiderGriddingPositionRecord("A-Race", 7, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 1, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 2, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 3, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 4, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 5, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 6, "", ""),
                        new RiderGriddingPositionRecord("B-Race", 7, "", ""),
                        new RiderGriddingPositionRecord("Women", 1, "", ""),
                        new RiderGriddingPositionRecord("Women", 2, "", ""),
                        new RiderGriddingPositionRecord("Women", 3, "", ""),
                        new RiderGriddingPositionRecord("Women", 4, "", ""),
                        new RiderGriddingPositionRecord("Women", 5, "", ""),
                        new RiderGriddingPositionRecord("Women", 6, "", ""),
                        new RiderGriddingPositionRecord("Women", 7, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Male", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Male", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Male", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Male", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Male", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Male", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Male", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Male", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Male", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Female", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Female", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Female", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 16s Female", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Female", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Female", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Female", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 14s Female", 4, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Female", 1, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Female", 2, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Female", 3, "", ""),
                        new RiderGriddingPositionRecord("Under 12s Female", 4, "", ""))),
                "https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/");
    }

    @Test
    void gridRace() {
        given()
                .contentType(ContentType.JSON)
                .body(new GriddingRequestRecord("https://docs.google.com/spreadsheets/d/1tQCo5fMjuTVzHUkKvJO9T5BMu9n_Pb0dGgu0Rz-zVpE/", "https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/", 2))
                .when()
                .post("/gridding")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("gridding", equalTo("https://docs.google.com/spreadsheets/d/1cEckJyAnjl8eUrh_BaT6hvXRzwTzL7OLxl2kpqGmvec/"),
                        "errorMessage", emptyString());
    }
}