package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SheetsQuickstartTest {

    @Autowired
    SheetsQuickstart sheetsQuickstart;

    @Test
    void namesAndMajors() throws GeneralSecurityException, IOException {
        String actual = sheetsQuickstart.namesAndMajors();
        String expected = """
                Name, Major
                Alexandra, English
                """;
        assertEquals(expected, actual);
    }
}