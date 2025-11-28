package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.dto.UpdateStandingsRequestRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.testutils.TestUtils;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StandingsControllerTest {

    @Autowired
    TestUtils testUtils;
    private final Logger logger = LoggerFactory.getLogger(StandingsControllerTest.class);

    @BeforeAll
    static void beforeClass() {
        baseURI = "http://localhost:8080/";
    }

    // Comment out this Annotation to enable output monitoring. But remember to manually clean out the file.
    @AfterEach
    void tearDown() {
        logger.info("Cleaning up after test.");
        //testUtils.wipeStandingsSheet("test_sheet");
    }

    @Test
    void updateStandings() {
        given()
                .contentType(ContentType.JSON)
                .body(new UpdateStandingsRequestRecord("https://docs.google.com/spreadsheets/d/1CUxbgIU_gEIu3-ZKV0OD0nNM8TrvFZewf5PbXlf2fsA/", 1))
                .when()
                .post("/updatestandings")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("standingsSheetUrl", equalTo("https://docs.google.com/spreadsheets/d/1wdziPkrBf1Yxz7ugLA7K4TjYLPYY8DOOJVK569gSiPs/"));
    }

    @Test
    public void griddingExceptions() {
        given()
                .contentType(ContentType.JSON)
                .body(new UpdateStandingsRequestRecord("https://turtle", 1))
                .when()
                .post("/updatestandings")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("title", equalTo("Update Standings Error"))
                .body("detail", equalTo("Results Google Sheet is not a valid URL."))
                .body("errorCode", equalTo("UPDATE_STANDINGS_ERROR"));
    }

    @Test
    public void griddingValidation() {
        given()
                .contentType(ContentType.JSON)
                .body(new UpdateStandingsRequestRecord("", 0))
                .when()
                .post("/updatestandings")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("title", equalTo("Validation Failed"))
                .body("errors.roundResultsUrl", equalTo("roundResultsUrl must not be blank"))
                .body("errors.roundNumber", equalTo("roundNumber must be 1, or greater"));
    }
}