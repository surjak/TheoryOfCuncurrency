package com.surjak.lab5;

import java.util.concurrent.ExecutionException;

/**
 * Created by surja on 03.11.2020
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Mandelbrot mandelbrot = new Mandelbrot();
        mandelbrot.setVisible(true);
    }
}
