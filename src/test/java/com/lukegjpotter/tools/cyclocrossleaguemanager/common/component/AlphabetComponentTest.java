package com.lukegjpotter.tools.cyclocrossleaguemanager.common.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AlphabetComponentTest {

    @Autowired
    AlphabetComponent alphabetComponent;

    @Test
    void positionInAlphabet_LetterA_0() {
        int expected = 0;
        int actual = alphabetComponent.positionInAlphabet("A");
        assertEquals(expected, actual);
    }

    @Test
    void positionInAlphabet_LetterAA_26() {
        int expected = 26;
        int actual = alphabetComponent.positionInAlphabet("AA");
        assertEquals(expected, actual);
    }

    @Test
    void positionInAlphabet_SpecialCharacter_Minus1() {
        int expected = -1;
        int actual = alphabetComponent.positionInAlphabet("!");
        assertEquals(expected, actual);
    }

    @Test
    void lettersInAlphabet_A0() {
        String expected = "A";
        String actual = alphabetComponent.lettersInAlphabet().get(0);
        assertEquals(expected, actual);
    }
}