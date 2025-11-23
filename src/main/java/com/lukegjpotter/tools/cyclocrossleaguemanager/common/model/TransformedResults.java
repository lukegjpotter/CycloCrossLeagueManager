package com.lukegjpotter.tools.cyclocrossleaguemanager.common.model;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.exception.ResultNotFoundException;
import com.lukegjpotter.tools.cyclocrossleaguemanager.standings.model.ResultRowRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransformedResults {

    private String round;
    private final List<ResultRowRecord> resultRows;

    public TransformedResults() {
        resultRows = new ArrayList<>();
    }

    public void addResultRow(String raceCategory, int position, String fullName, String club, String ageCategory, String gender) {
        resultRows.add(new ResultRowRecord(raceCategory, position, fullName, club, ageCategory, gender));
    }

    public ResultRowRecord findResultRowForPositionAndRaceNameAndGender(String raceCategory, int position, String gender) throws ResultNotFoundException {
        return resultRows
                .stream()
                .filter(resultRow -> resultRow.position() == position
                        && resultRow.raceCategory().equals(raceCategory)
                        && resultRow.gender().equals(gender))
                .findFirst()
                .orElseThrow(() -> new ResultNotFoundException("Result not found for criteria: " + raceCategory + ", " + position + ", " + gender + "."));
    }

    public List<ResultRowRecord> findAllResultRowsForRaceNameAndGender(String raceCategory, String gender) {
        return resultRows
                .stream()
                .filter(resultRow -> resultRow.raceCategory().equals(raceCategory) && resultRow.gender().equals(gender))
                .collect(Collectors.toList());
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }
}
