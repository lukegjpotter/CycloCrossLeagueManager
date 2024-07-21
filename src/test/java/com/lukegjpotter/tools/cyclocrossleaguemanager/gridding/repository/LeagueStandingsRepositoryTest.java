package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class LeagueStandingsRepositoryTest {

    @Autowired
    LeagueStandingsRepository leagueStandingsRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAll() {
        fail("Not Implemented");
    }

    @Test
    void findSignups() {
        fail("Not Implemented");
    }
}