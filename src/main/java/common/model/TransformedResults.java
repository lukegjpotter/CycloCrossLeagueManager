package common.model;

import common.exception.ResultNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransformedResults {

    private String round;
    private final List<ResultRow> resultRows;

    public TransformedResults() {
        resultRows = new ArrayList<>();
    }

    public void addResultRow(int position, String name, String club, String category, String raceName, String gender) {
        resultRows.add(new ResultRow(position, name, club, category, raceName, gender));
    }

    public ResultRow findResultRowForPositionAndRaceNameAndGender(int position, String raceName, String gender) throws ResultNotFoundException {
        return resultRows
                .stream()
                .filter(resultRow -> resultRow.position() == position
                        && resultRow.raceName().equals(raceName)
                        && resultRow.gender().equals(gender))
                .findFirst()
                .orElseThrow(() -> new ResultNotFoundException("Result not found for criteria: " + position + ", " + raceName + ", " + gender + "."));
    }

    public List<ResultRow> findAllResultRowsForRaceNameAndGender(String raceName, String gender) {
        return resultRows
                .stream()
                .filter(resultRow -> resultRow.raceName().equals(raceName) && resultRow.gender().equals(gender))
                .collect(Collectors.toList());
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }
}
