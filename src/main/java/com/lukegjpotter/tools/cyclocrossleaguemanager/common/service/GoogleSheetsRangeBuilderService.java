package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.component.AlphabetComponent;
import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.RangeAndMinimumIndexRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.exception.GriddingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class GoogleSheetsRangeBuilderService {

    private final AlphabetComponent alphabetComponent;

    public GoogleSheetsRangeBuilderService(AlphabetComponent alphabetComponent) {
        this.alphabetComponent = alphabetComponent;
    }

    public RangeAndMinimumIndexRecord buildGoogleSheetsRange(String sheetName, Stream<Integer> indices) {

        StringBuilder range = new StringBuilder(sheetName).append("!");
        List<String> alphabet = alphabetComponent.lettersInAlphabet();
        List<Integer> positiveIndices = indices.filter(index -> index >= 0).toList();

        if (positiveIndices.isEmpty()) throw new GriddingException("Unable to find column names in Google Sheet.");

        int minIndex = positiveIndices.stream().min(Integer::compareTo).get();
        int maxIndex = positiveIndices.stream().max(Integer::compareTo).get();
        range.append(alphabet.get(minIndex)).append("2:").append(alphabet.get(maxIndex));

        return new RangeAndMinimumIndexRecord(range.toString(), minIndex);
    }
}
