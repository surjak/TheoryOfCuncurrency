package com.surjak.lab7.v1;

import java.util.Objects;

/**
 * Created by surjak on 04.12.2020
 */
public class Philosopher implements Runnable {
    private final int id;
    private final Table table;
    private int forkCount = 0;
    private boolean keepRunning = true;

    public Philosopher(int id, Table table) {
        this.id = id;
        this.table = Objects.requireNonNull(table);
    }

    @Override
    public void run() {
        while (keepRunning) {
            think();
            takeForks();
            eat();
            giveForks();
        }
    }

    public int getId() {
        return id;
    }

    private void giveForks() {
        table.giveForks(id);
    }

    private void takeForks() {
        table.takeForks(id);
    }

    private void eat() {
//        System.out.println("I am eating, id: " + id);
        try {
            long v = 10;
            Thread.sleep(v);
//            System.out.println("I have just finished eating, id: " + id);
            this.forkCount++;
            Stats.add(id, forkCount);
        } catch (InterruptedException e) {
        }
    }

    private void think() {
//        System.out.println("I am thinking, id: " + id);
        try {
            long v = 10;
            Thread.sleep(v);
        } catch (InterruptedException e) {
        }
    }

    public void interrupt() {
        this.keepRunning = false;
    }
}
