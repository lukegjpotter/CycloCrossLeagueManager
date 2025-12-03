package com.lukegjpotter.tools.cyclocrossleaguemanager.common.component;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Component
public class AlphabetComponent {

    HashMap<String, Integer> alphabetHashMap;

    public AlphabetComponent() {

        this.alphabetHashMap = new HashMap<>();
        alphabetHashMap.put("A", 0);
        alphabetHashMap.put("B", 1);
        alphabetHashMap.put("C", 2);
        alphabetHashMap.put("D", 3);
        alphabetHashMap.put("E", 4);
        alphabetHashMap.put("F", 5);
        alphabetHashMap.put("G", 6);
        alphabetHashMap.put("H", 7);
        alphabetHashMap.put("I", 8);
        alphabetHashMap.put("J", 9);
        alphabetHashMap.put("K", 10);
        alphabetHashMap.put("L", 11);
        alphabetHashMap.put("M", 12);
        alphabetHashMap.put("N", 13);
        alphabetHashMap.put("O", 14);
        alphabetHashMap.put("P", 15);
        alphabetHashMap.put("Q", 16);
        alphabetHashMap.put("R", 17);
        alphabetHashMap.put("S", 18);
        alphabetHashMap.put("T", 19);
        alphabetHashMap.put("U", 20);
        alphabetHashMap.put("V", 21);
        alphabetHashMap.put("W", 22);
        alphabetHashMap.put("X", 23);
        alphabetHashMap.put("Y", 24);
        alphabetHashMap.put("Z", 25);
        alphabetHashMap.put("AA", 26);
        alphabetHashMap.put("AB", 27);
        alphabetHashMap.put("AC", 28);
        alphabetHashMap.put("AD", 29);
        alphabetHashMap.put("AE", 30);
        alphabetHashMap.put("AF", 31);
        alphabetHashMap.put("AG", 32);
        alphabetHashMap.put("AH", 33);
        alphabetHashMap.put("AI", 34);
        alphabetHashMap.put("AJ", 35);
        alphabetHashMap.put("AK", 36);
        alphabetHashMap.put("AL", 37);
        alphabetHashMap.put("AM", 38);
        alphabetHashMap.put("AN", 39);
        alphabetHashMap.put("AO", 40);
        alphabetHashMap.put("AP", 41);
        alphabetHashMap.put("AQ", 42);
        alphabetHashMap.put("AR", 43);
        alphabetHashMap.put("AS", 44);
        alphabetHashMap.put("AT", 45);
        alphabetHashMap.put("AU", 46);
        alphabetHashMap.put("AV", 47);
        alphabetHashMap.put("AW", 48);
        alphabetHashMap.put("AX", 49);
        alphabetHashMap.put("AY", 50);
        alphabetHashMap.put("AZ", 51);
    }

    public int positionInAlphabet(String letter) {
        return alphabetHashMap.getOrDefault(letter.toUpperCase(), -1);
    }

    public List<String> lettersInAlphabet() {
        List<String> letters = new ArrayList<>(alphabetHashMap.keySet().stream().toList());
        letters.sort(Comparator.comparing(String::length));
        return letters;
    }
}
