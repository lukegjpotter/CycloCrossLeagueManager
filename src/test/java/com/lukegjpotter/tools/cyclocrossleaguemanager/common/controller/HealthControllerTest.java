package com.lukegjpotter.tools.cyclocrossleaguemanager.common.controller;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HealthControllerTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:8080/";
    }

    @Test
    void health() {
        RestAssured.when().get("health")
                .then().statusCode(HttpStatus.SC_OK)
                .body(Matchers.equalTo("alive"));
    }
}