package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model;

public record RiderNamePointsAndCellRecord(String riderFullName, String club, String ageCategory, int points,
                                           String cellToUpdate, boolean requiresNewRiderEntry) {
}
