package com.surjak.lab4;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by surja on 20.10.2020
 */
public class Histogram {
    private Map<String, Integer> histogram = new HashMap<>();
    private Map<String, Integer> entryMap = new HashMap<>();
    private Displayer displayer;

    public Histogram(Displayer displayer) {
        this.displayer = displayer;
    }

    public synchronized void add(String key, Integer value) {
        entryMap.put(key, value);
    }

    public synchronized void add(String key) {
        if (histogram.get(key) == null) {
            histogram.put(key, 1);
        } else {
            Integer integer = histogram.get(key);
            histogram.put(key, integer + 1);
        }
    }

    public synchronized void drawHistogram() {
        displayer.display(histogram);
    }

    public synchronized void drawHistogramFromEntryMap() {
        displayer.display(entryMap);
    }
}
