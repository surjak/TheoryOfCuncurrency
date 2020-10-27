package com.surjak.lab4;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by surja on 20.10.2020
 */
public class HistogramDisplayer implements Displayer {
    @Override
    public void display(Map<String, Integer> map) {
        List<CSVRow> list = map
                .entrySet()
                .stream()
                .map(e -> new CSVRow(Main.M, e.getKey().split("-")[0], Integer.parseInt(e.getKey().split("-")[1]), Main.numberOfProducers, Main.fairBuffer, Main.probability, e.getValue()))
                .collect(Collectors.toList());
        list.forEach(System.out::println);
    }

}
