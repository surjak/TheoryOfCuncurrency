package com.surjak.lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by surja on 20.10.2020
 */
public class Main {

    public static final int M = 100;
    private static final int numberOfConsumers = 10;
    private static final int numberOfProducers = 10;

    public static void main(String[] args) {
        Buffer buffer = new Buffer(2 * M);
        List<Producer> producers = new ArrayList<>();
        List<Consumer> consumers = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfProducers + numberOfProducers);

        for (int i =0; i< numberOfConsumers; i++) {
            Producer producer = new Producer(buffer, M);
            Consumer consumer = new Consumer(buffer, M);
            executorService.submit(producer);
            executorService.submit(consumer);
            producers.add(producer);
            consumers.add(consumer);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdownNow();


    }
}
