package com.surjak.lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by surja on 20.10.2020
 */
public class Main {

    public static final int M = 10000;
    public static final int numberOfConsumers = 10;
    public static final int numberOfProducers = 10;
    public static final boolean probability = true;
    public static final boolean fairBuffer = true;

    public static void main(String[] args) throws InterruptedException {
        IBuffer buffer;
        if (fairBuffer) {
            buffer = new Buffer(M);
        } else {
            buffer = new BufferNaive(M);
        }
        Histogram histogram = new Histogram(new HistogramDisplayer());
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfProducers + numberOfProducers);

        for (int i = 0; i < numberOfConsumers; i++) {
            Producer producer = new Producer(buffer, histogram, M / 2, probability);
            Consumer consumer = new Consumer(buffer, histogram, M / 2, probability);
            executorService.submit(producer);
            executorService.submit(consumer);
        }
        System.out.println("Running...");
        System.out.println("Click enter to draw Histogram");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); //wait for user Input

        executorService.shutdownNow();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        histogram.drawHistogram();
//        histogram.drawHistogramFromEntryMap();

    }

    private static void waitSomeTime(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
