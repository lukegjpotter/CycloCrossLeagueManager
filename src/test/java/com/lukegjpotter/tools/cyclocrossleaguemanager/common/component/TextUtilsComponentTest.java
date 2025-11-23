package com.lukegjpotter.tools.cyclocrossleaguemanager.common.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TextUtilsComponentTest {

    @Autowired
    TextUtilsComponent textUtilsComponent;

    @Test
    void toTitleCase_UpperCaseSurnames() {
        List<String> actual = List.of(
                textUtilsComponent.toTitleCase("Sean UPPERCASE"),
                textUtilsComponent.toTitleCase("SEAN UPPERCASE"),
                textUtilsComponent.toTitleCase("SEAN Uppercase"),
                textUtilsComponent.toTitleCase("john lowercase"),
                textUtilsComponent.toTitleCase("John Titlecase"),
                textUtilsComponent.toTitleCase("J-Jay Car-Parker")
        );

        List<String> expected = List.of(
                "Sean Uppercase",
                "Sean Uppercase",
                "Sean Uppercase",
                "John Lowercase",
                "John Titlecase",
                "J-Jay Car-Parker"
        );

        assertEquals(expected, actual);
    }

    @Test
    void toTitleCase_IrishSurnames() {
        List<String> actual = List.of(
                textUtilsComponent.toTitleCase("Sean MC UPPERCASE"),
                textUtilsComponent.toTitleCase("SEAN Mc UPPERCASE"),
                textUtilsComponent.toTitleCase("SEAN O'Uppercase"),
                textUtilsComponent.toTitleCase("john o lowercase")
        );

        List<String> expected = List.of(
                "Sean Mc Uppercase",
                "Sean Mc Uppercase",
                "Sean O'Uppercase",
                "John O Lowercase"
        );

        assertEquals(expected, actual);
    }

    @Test
    void toTitleCase_RaceCategoryNames() {
        List<String> actual = List.of(
                textUtilsComponent.toTitleCase("JUNIOR"),
                textUtilsComponent.toTitleCase("SENIOR"),
                textUtilsComponent.toTitleCase("Junior"),
                textUtilsComponent.toTitleCase("senior")
        );

        List<String> expected = List.of(
                "Junior",
                "Senior",
                "Junior",
                "Senior"
        );

        assertEquals(expected, actual);
    }

    @Test
    void toIrishFormattedName_IrishSurnames() {
        List<String> actual = List.of(
                textUtilsComponent.toIrishFormattedName("Sean McUPPERCASE"),
                textUtilsComponent.toIrishFormattedName("Sean McMCPERCASE"),
                textUtilsComponent.toIrishFormattedName("SEAN MacUPPERCASE"),
                textUtilsComponent.toIrishFormattedName("SEAN O'TitleCase"),
                textUtilsComponent.toIrishFormattedName("John Swift"),
                textUtilsComponent.toIrishFormattedName("John Ó'Tuath"),
                textUtilsComponent.toIrishFormattedName("john o lowercase")
        );

        List<String> expected = List.of(
                "Sean Mc UPPERCASE",
                "Sean Mc MCPERCASE",
                "SEAN Mac UPPERCASE",
                "SEAN O TitleCase",
                "John Swift",
                "John Ó Tuath",
                "john o lowercase"
        );

        assertEquals(expected, actual);
    }

    @Test
    void toIrishFormattedNameAndTitleCase() {
        List<String> actual = List.of(
                textUtilsComponent.toIrishFormattedNameAndTitleCase("Sean MCUPPERCASE"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("Sean McUPPERCASE"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("Sean McMCPERCASE"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("SEAN MacUPPERCASE"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("John Mckenna"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("John Mc Kenna"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("SEAN O'TitleCase"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("John Ó'Tuath"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("john o lowercase"),
                textUtilsComponent.toIrishFormattedNameAndTitleCase("John Swift")
        );

        List<String> expected = List.of(
                "Sean Mc Uppercase",
                "Sean Mc Uppercase",
                "Sean Mc Mcpercase",
                "Sean Mac Uppercase",
                "John Mc Kenna",
                "John Mc Kenna",
                "Sean O Titlecase",
                "John Ó Tuath",
                "John O Lowercase",
                "John Swift"
        );

        assertEquals(expected, actual);
    }
}