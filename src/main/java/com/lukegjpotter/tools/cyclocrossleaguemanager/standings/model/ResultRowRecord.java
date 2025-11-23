package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model;

public record ResultRowRecord(
        /**
         * @code[raceCategory] is the name of the Sheet in the Google Sheet.
         * For Example: A-Race, B-Race, Women, Youth, Underage.
         */
        String raceCategory,
        int position,
        String fullName,
        String club,
        /**
         * @code[ageCategory] is the name of the rider's age category.
         * For Example: Junior, Senior, M40, M50, M60, Under  16, Under 14, Under 12.
         */
        String ageCategory,
        String gender) {
}
