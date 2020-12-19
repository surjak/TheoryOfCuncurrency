package com.surjak.lab1;

/**
 * Created by surja on 06.10.2020
 */
public class Counter {
    private int counter = 0;

    public synchronized void increment() {
        this.counter++;
    }

    public synchronized void decrement() {
        this.counter--;
    }

    public synchronized int getCounterValue() {
        return this.counter;
    }

}
