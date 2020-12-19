package com.surjak.lab3;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by surja on 20.10.2020
 */
public class HistogramDisplayer implements Displayer {
    @Override
    public void display(Map<String, Integer> map) {
        map.keySet().stream()
                .sorted(this::compareHistogramStrings)
                .collect(Collectors.toList())
                .forEach(s -> System.out.println(s+" "+map.get(s)));
    }

    private int compareHistogramStrings(String a, String b) {

        String[] splitA = a.split("-");
        String a1 = splitA[1];
        String a0 = splitA[0];

        String[] splitB = b.split("-");
        String b1 = splitB[1];
        String b0 = splitB[0];

        int compareResult = a0.compareTo(b0);
        if (compareResult == 0) {
            return Integer.parseInt(a1) < Integer.parseInt(b1) ? -1 : 1;
        } else return compareResult;
    }
}
