package com.surjak.lab5;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.util.concurrent.*;
import javax.swing.JFrame;

public class Mandelbrot extends JFrame {

    private final int MAX_ITER = 1570;
    private final double ZOOM = 1500;
    private final int numOfThreads = 100;
    private BufferedImage I;
    private final int taskCount = numOfThreads * 10;

    public Mandelbrot() throws ExecutionException, InterruptedException {
        super("Mandelbrot Set");
        setBounds(100, 100, 1000, 1000);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);


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


    }

    private void drawPartOfImage(int height, int dy) {

        for (int y = height - dy; y < height; y++) {
            for (int x = 0; x < getWidth(); x++) {
                double zx =0;
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

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }


}