package common.controller;

import griding.service.GridingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GridingControllerTest {

    @InjectMocks
    GridingController gridingController;
    @Mock
    GridingService gridingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void gridRace() {
        String signups = "";
        Mockito.doNothing().when(gridingService).gridSignups(signups);

        String expected = "";
        String actual = gridingController.gridRace(signups);

        assertEquals(expected, actual);
    }
}