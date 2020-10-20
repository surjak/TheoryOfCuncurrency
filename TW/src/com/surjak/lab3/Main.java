package com.surjak.lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by surja on 20.10.2020
 */
public class Main {

    public static final int M = 1000;
    private static final int numberOfConsumers = 10;
    private static final int numberOfProducers = 10;

    public static void main(String[] args) throws InterruptedException {
        Buffer buffer = new Buffer(2 * M);
        Histogram histogram = new Histogram(new HistogramDisplayer());
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfProducers + numberOfProducers);

        for (int i =0; i< numberOfConsumers; i++) {
            //int putRandom = new Random().nextInt(M);
            //int popRandom = new Random().nextInt(M);
            int putRandom = (int) Math.pow(2, i);
            int popRandom = (int) Math.pow(2, i);

            Producer producer = new Producer(buffer,histogram, putRandom);
            Consumer consumer = new Consumer(buffer,histogram, popRandom);
            executorService.submit(producer);
            executorService.submit(consumer);
        }

        waitSomeTime(2);

        executorService.shutdownNow();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        histogram.drawHistogram();

    }

    private static void waitSomeTime(int seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
