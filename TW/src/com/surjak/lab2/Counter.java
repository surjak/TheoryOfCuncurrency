package com.surjak.lab2;

/**
 * Created by surja on 13.10.2020
 */
public class Counter {
    private int n = 1;
    private int maxN;

    public Counter(int maxN) {
        this.maxN = maxN;
    }

    public void changeN(int n) {
        this.n = n;
        if (n > maxN)
            this.n = 1;
    }

    public int getValue() {
        return this.n;
    }

}
