package com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model;

import java.util.HashMap;

public record LeagueStandingsAndRoundColumnRecord(HashMap<String, RiderNameAndCellRecord> leagueStandingsHashMap,
                                                  HashMap<String, Character> roundColumnLetterHashMap) {
}
