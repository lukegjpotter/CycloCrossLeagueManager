package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.dto.GriddingRequestRecord;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GriddingControllerTest {

    @BeforeAll
    static void beforeClass() {
        baseURI = "http://localhost:8080/";
    }

    @Test
    void gridRace() {
        given()
                .contentType(ContentType.JSON)
                .body(new GriddingRequestRecord("docs.google.com/spreadsheet/123", "docs.google.com/spreadsheet/456", 1))
                .when()
                .post("/gridding")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("gridding", equalTo("docs.google.com/spreadsheet/456"),
                        "errorMessage", emptyString());
    }
}