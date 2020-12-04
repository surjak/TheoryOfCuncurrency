package com.surjak.lab7.v1;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by surjak on 04.12.2020
 */
public class Stats {
    public static Map<Integer, Integer> getPhilosopherForkCount() {
        return philosopherForkCount;
    }

    private static final Map<Integer, Integer> philosopherForkCount = new HashMap<>();

    public static void add(Integer id, Integer count) {
        philosopherForkCount.put(id, count);
    }
}
