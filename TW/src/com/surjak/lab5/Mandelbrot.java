package com.surjak.lab5;

import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame {

    private final int MAX_ITER = 1570;
    private final double ZOOM = 500;
    private int numOfThreads = 10;
    private BufferedImage I;
    private int taskCount = numOfThreads * 10;
    private List<String> timeList = new ArrayList<>();
    private List<Integer> numOfThreadsArray = List.of(1, 2, 5, 10, 20, 25, 50, 100);
    private boolean finalTaskCount = false;
    private boolean exactThreadsAndTasks = false;

    public Mandelbrot() throws ExecutionException, InterruptedException {
        super("Mandelbrot Set");
        setBounds(100, 100, 1000, 1000);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void run() {
        for (int j : numOfThreadsArray) {
            numOfThreads = j;
            if (!finalTaskCount)
                taskCount = numOfThreads * 10;
            if(exactThreadsAndTasks)
                taskCount = j;

            I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

            long startTimeMillis = System.currentTimeMillis();
            ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
            List<Runnable> tasks = new ArrayList<>();
            for (int i = 1; i <= taskCount; i++) {
                int height = getHeight() / taskCount * i;
                tasks.add(() -> drawPartOfImage(height, getHeight() / taskCount));
            }
            CompletableFuture<?>[] futures = tasks.stream()
                    .map(task -> CompletableFuture.runAsync(task, es))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).join();
            es.shutdown();
            long endTimeMillis = System.currentTimeMillis();
            timeList.add("For " + numOfThreads + " threads and " + taskCount + " tasks: " + (endTimeMillis - startTimeMillis) + " milliseconds");
        }
    }


    private void drawPartOfImage(int height, int dy) {

        for (int y = height - dy; y < height; y++) {
            for (int x = 0; x < getWidth(); x++) {
                double zx = 0;
                double zy = 0;
                double cX = (x - 400) / ZOOM;
                double cY = (y - 300) / ZOOM;
                int iter = MAX_ITER;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    double tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                }
                synchronized (this) {
                    I.setRGB(x, y, iter | (iter << 8));
                }
            }
        }
    }

    public void printTimeMap() {
        timeList.stream()
                .forEach(System.out::println);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }


    public void setFinalTaskCount(int i) {
        this.taskCount = i;
        this.finalTaskCount = true;
    }

    public void setNumOfThreadsArray(List<Integer> numOfThreadsArray) {
        this.numOfThreadsArray = numOfThreadsArray;
    }

    public void setExactNumOfThreadsAndTasks(boolean exact) {
        if(exact && finalTaskCount){
            throw new RuntimeException();
        }
        this.exactThreadsAndTasks = exact;
    }
}