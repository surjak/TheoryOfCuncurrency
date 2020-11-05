package com.surjak.lab5;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by surja on 03.11.2020
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Mandelbrot mandelbrot = new Mandelbrot();
        mandelbrot.run();
        mandelbrot.printTimeMap();

        System.out.println("****************************************");

        Mandelbrot mandelbrot2 = new Mandelbrot();
        mandelbrot2.setFinalTaskCount(100);
        List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50, 60, 70, 80, 90, 100);
        mandelbrot2.setNumOfThreadsArray(integers);
        mandelbrot2.run();
        mandelbrot2.printTimeMap();

        System.out.println("****************************************");

        Mandelbrot mandelbrot3 = new Mandelbrot();
        mandelbrot3.setExactNumOfThreadsAndTasks(true);
        mandelbrot3.setNumOfThreadsArray(integers);
        mandelbrot3.run();
        mandelbrot3.printTimeMap();

    }
}
