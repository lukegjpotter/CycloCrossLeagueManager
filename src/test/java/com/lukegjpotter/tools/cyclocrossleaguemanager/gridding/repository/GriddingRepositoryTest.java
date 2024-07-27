package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class GriddingRepositoryTest {

    @Autowired
    GriddingRepository griddingRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void writeGriddingToGoogleSheet() {
        fail("Not Implemented");
    }
}