package com.lukegjpotter.tools.cyclocrossleaguemanager.common.service;

import com.lukegjpotter.tools.cyclocrossleaguemanager.common.model.RangeAndMinimumIndexRecord;
import com.lukegjpotter.tools.cyclocrossleaguemanager.gridding.exception.GriddingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class GoogleSheetsRangeBuilderService {

    public RangeAndMinimumIndexRecord buildGoogleSheetsRange(String sheetName, Stream<Integer> indices) {

        StringBuilder range = new StringBuilder(sheetName).append("!");
        List<String> alphabet = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH");
        List<Integer> positiveIndices = indices.filter(index -> index >= 0).toList();

        if (positiveIndices.isEmpty()) throw new GriddingException("Unable to find column names in Google Sheet.");

        int minIndex = positiveIndices.stream().min(Integer::compareTo).get();
        int maxIndex = positiveIndices.stream().max(Integer::compareTo).get();
        range.append(alphabet.get(minIndex)).append("2:").append(alphabet.get(maxIndex));

        return new RangeAndMinimumIndexRecord(range.toString(), minIndex);
    }
}
