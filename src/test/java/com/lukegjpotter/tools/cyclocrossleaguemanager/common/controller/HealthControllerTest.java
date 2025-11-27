package com.lukegjpotter.tools.cyclocrossleaguemanager.common.controller;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HealthControllerTest {

    @BeforeAll
    static void beforeClass() {
        baseURI = "http://localhost:8080/";
    }

    @Test
    void health() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/health")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("status", equalTo("alive"));
    }
}