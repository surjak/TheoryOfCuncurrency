package com.surjak.lab2;

/**
 * Created by surja on 13.10.2020
 */
public class ThreadNumberPrinter extends Thread {
    private final int iterationsCount = 10;
    private final Counter counter;
    private final int myNumber;

    public ThreadNumberPrinter(Counter counter, int n) {
        this.counter = counter;
        this.myNumber = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < iterationsCount; i++) {
            printAndChangeCounterValue();
        }
    }

    private void printAndChangeCounterValue(){
        synchronized (counter) {
            while (!myTurn())
                waitForNotification();
            System.out.print(this.counter.getValue());
            this.counter.changeN(this.myNumber + 1);
            counter.notifyAll();
        }
    }

    private boolean myTurn(){
        return counter.getValue() == myNumber;
    }

    private void waitForNotification() {
        try {
            counter.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
