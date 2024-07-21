package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GriddingControllerTest {

    @BeforeAll
    static void beforeClass() {
        baseURI = "http://localhost:8080/lotto";
    }

    @Test
    void gridRace() {
        given()
                .body(new GriddingRequestRecord("docs.google.com/spreadsheet/123"))
                .when()
                .post("/gridding")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("googleSheet", contains("docs.google.com/spreadsheet/456"));
    }
}