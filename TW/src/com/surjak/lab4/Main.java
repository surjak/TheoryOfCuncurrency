package com.surjak.lab4;

import java.io.IOException;
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

    public static int M = 10000;
    public static int numberOfConsumers = 10;
    public static int numberOfProducers = 10;
    public static boolean probability = true;
    public static boolean fairBuffer = true;

    public static void main(String[] args) throws InterruptedException, IOException {

        HistogramDisplayer histogramDisplayer = new HistogramDisplayer();
        histogramDisplayer.writeHeader();

        for (int _numOfPC : List.of(100, 1000)) {
            for (int _M : List.of(10000, 100000)) {
                for (boolean _probability : List.of(true, false)) {
                    for (boolean _fairBuffer : List.of(true, false)) {
                        numberOfProducers = _numOfPC;
                        numberOfConsumers = _numOfPC;
                        M = _M;
                        probability = _probability;
                        fairBuffer = _fairBuffer;
                        run(histogramDisplayer);
                    }
                }
            }
        }


    }

    private static void run(HistogramDisplayer histogramDisplayer) throws InterruptedException, IOException {


        IBuffer buffer;
        if (fairBuffer) {
            buffer = new Buffer(M);
        } else {
            buffer = new BufferNaive(M);
        }

        Histogram histogram = new Histogram(histogramDisplayer);

        ExecutorService executorService = Executors.newFixedThreadPool(2 * numberOfProducers + numberOfProducers);

        for (int i = 0; i < numberOfConsumers; i++) {
            Producer producer = new Producer(buffer, histogram, M / 2, probability);
            Consumer consumer = new Consumer(buffer, histogram, M / 2, probability);
            executorService.submit(producer);
            executorService.submit(consumer);
        }
//        System.out.println("Running...");
//        System.out.println("Click enter to draw Histogram");
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine(); //wait for user Input
        waitSomeTime(1);

        executorService.shutdownNow();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        histogram.drawHistogram();
    }

    private static void waitSomeTime(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
