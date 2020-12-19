package com.surjak.lab1;

import java.util.concurrent.*;

public class Race {

    private static final int iterationsCount = 10000000;

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {

        Counter counter = new Counter();

        CounterThread counterIncrementingThread = new CounterThread(counter::increment, iterationsCount);
        CounterThread counterDecrementingThread = new CounterThread(counter::decrement, iterationsCount);

        counterIncrementingThread.start();
        counterDecrementingThread.start();

        counterIncrementingThread.join();
        counterDecrementingThread.join();

        System.out.println(counter.getCounterValue());

    }
}
