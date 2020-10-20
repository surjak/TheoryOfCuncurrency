package com.surjak.lab3;

import java.util.Random;

/**
 * Created by surja on 20.10.2020
 */
public class Producer implements Runnable {
    private Buffer buffer;
    int size;
    int successfulTry = 0;

    public Producer(Buffer buffer, int M) {
        this.buffer = buffer;
        size = new Random().nextInt(M);
    }

    @Override
    public void run() {

        while (true) {
            buffer.pop(size);
            this.successfulTry++;
            if(Thread.currentThread().isInterrupted()){
                System.out.println("Exiting" + size);
//                buffer.writeCondition.signalAll();
//                buffer.readCondition.signalAll();
                return;
            }
        }
    }

}
