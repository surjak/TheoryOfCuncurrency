package com.surjak.lab1;

import java.util.stream.IntStream;

/**
 * Created by surja on 06.10.2020
 */
public class CounterThread extends Thread {

    private final Runnable runnable;
    private final int times;

    public CounterThread(Runnable runnable, int times) {
        this.runnable = runnable;
        this.times = times;
    }

    @Override
    public void run() {
        IntStream.range(0, times).forEach(i -> runnable.run());
    }
}
