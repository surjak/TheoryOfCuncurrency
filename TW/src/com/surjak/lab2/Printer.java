package com.surjak.lab2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by surja on 13.10.2020
 */
public class Printer {

    public static final int threadCount = 3;

    public static void main(String[] args) {
        Counter counter = new Counter(threadCount);
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= threadCount; i++)
            threads.add(new ThreadNumberPrinter(counter, i));

        threads.forEach(Thread::start);
        threads.forEach(Printer::waitForThread);

    }

    private static void waitForThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
