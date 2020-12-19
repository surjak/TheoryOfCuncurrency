package com.surjak.lab3;

import java.util.Random;

/**
 * Created by surja on 20.10.2020
 */
public class Consumer implements Runnable {
    private Buffer buffer;
    private Histogram histogram;
    private final int size;
    private int counter = 0;


    public Consumer(Buffer buffer,Histogram histogram, int M) {
        this.buffer = buffer;
        this.size = M;
        this.histogram = histogram;
    }

    @Override
    public void run() {
        while (true) {
            buffer.pop(size);
            if(Thread.currentThread().isInterrupted()){
                histogram.add("CONSUMER-"+size, this.counter);
                return;
            }
            this.counter++;
        }
    }
}
