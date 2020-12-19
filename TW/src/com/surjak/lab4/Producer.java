package com.surjak.lab4;

import java.util.Random;

/**
 * Created by surja on 20.10.2020
 */
public class Producer implements Runnable {
    private IBuffer buffer;
    private Histogram histogram;
    private int size;
    private int counter = 0;
    private int maxSize;
    private boolean probability;

    public Producer(IBuffer buffer, Histogram histogram, int maxSize, boolean probability) {
        this.buffer = buffer;
        this.maxSize = maxSize;
        this.histogram = histogram;
        this.probability = probability;
    }

    @Override
    public void run() {
        while (true) {
            if (this.probability) {
                randSize();
            } else {
                randSizePro();
            }
            buffer.put(size);
            histogram.add("PRODUCER-" + size);
            if (Thread.currentThread().isInterrupted()) {
                histogram.add("PRODUCER-" + NumberProvider.getNumber(), this.counter);
                return;
            }
            this.counter++;
        }
    }

    private void randSizePro() {
        var randomizer = new Random();
        var randomDouble = randomizer.nextDouble();

        var result = Math.floor((maxSize + 1) * (Math.pow(randomDouble, 2)));
        this.size = (int) result;
        if (this.size == 0) this.size++;
    }

    private void randSize() {
        this.size = new Random().nextInt(maxSize);
        if (this.size == 0) this.size++;
    }
}
