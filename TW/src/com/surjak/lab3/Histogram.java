package com.surjak.lab3;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by surja on 20.10.2020
 */
public class Histogram {
    private Map<String, Integer> histogram = new HashMap<>();
    private Displayer displayer;

    public Histogram(Displayer displayer) {
        this.displayer = displayer;
    }

    public void add(String key, Integer value) {
        histogram.put(key, value);
    }

    public void drawHistogram() {
        displayer.display(histogram);
    }
}
