package com.lukegjpotter.tools.cyclocrossleaguemanager.common.component;

import org.springframework.stereotype.Component;

@Component
public class TextUtilsComponent {

    public String toIrishFormattedNameAndTitleCase(final String input) {
        return toTitleCase(toIrishFormattedName(input));
    }

    public String toTitleCase(final String input) {

        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c) || c == '\'' || c == '-') {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public String toIrishFormattedName(final String fullName) {

        if (fullName.contains("'")) return fullName.replace("'", " ");

        String surname = fullName.split(" ", 2)[1];
        if (surname.toLowerCase().startsWith("mc")) {
            surname = "Mc " + surname.substring(2);
        }
        if (surname.toLowerCase().startsWith("mac")) {
            surname = "Mac " + surname.substring(3);
        }

        return fullName.split(" ", 2)[0]
                + " "
                + surname.replace("  ", " ");
    }
}
