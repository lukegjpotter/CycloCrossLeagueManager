package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.controller;

import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.service.GriddingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GriddingControllerTest {

    @InjectMocks
    GriddingController griddingController;
    @Mock
    GriddingService griddingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void gridRace() {
        String signups = "";
        Mockito.doNothing().when(griddingService).gridSignups(signups);

        String expected = "";
        String actual = griddingController.griding(signups).getBody();

        assertEquals(expected, actual);
    }
}