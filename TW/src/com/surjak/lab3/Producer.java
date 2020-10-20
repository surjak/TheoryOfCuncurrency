package com.surjak.lab3;

import java.util.Random;

/**
 * Created by surja on 20.10.2020
 */
public class Producer implements Runnable {
    private Buffer buffer;
    private Histogram histogram;
    private final int size;
    private int counter = 0;

    public Producer(Buffer buffer,Histogram histogram, int M) {
        this.buffer = buffer;
        this.size = M;
        this.histogram = histogram;
    }

    @Override
    public void run() {
        while (true) {
            buffer.put(size);
            if(Thread.currentThread().isInterrupted()){
                histogram.add("PRODUCER-"+size, this.counter);
                return;
            }
            this.counter++;
        }
    }

}
