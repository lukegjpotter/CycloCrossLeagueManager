package com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.repository.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SheetsQuickstartTest {

    @Autowired
    SheetsQuickstart sheetsQuickstart;

    @Test
    void namesAndMajors() throws IOException {
        String actual = sheetsQuickstart.namesAndMajors();
        String expected = """
                Name, Gender
                Alexandra, Female
                """;
        assertEquals(expected, actual);
    }

    @Test
    void writeExample() throws IOException {
        String actual = sheetsQuickstart.writeExample(false);
        String expected = "'Class Data'!A2:B2";

        sheetsQuickstart.writeExample(true);

        assertEquals(expected, actual);
    }
}