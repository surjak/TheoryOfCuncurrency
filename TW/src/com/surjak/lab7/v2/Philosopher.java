package com.surjak.lab7.v2;

/**
 * Created by surjak on 04.12.2020
 */
public class Philosopher implements Runnable {

    private final int id;
    private final Table table;
    private boolean keepWorking = true;
    private int forkCount = 0;

    public Philosopher(int id, Table table) {
        this.id = id;
        this.table = table;
    }

    @Override
    public void run() {
        while (keepWorking) {
            think();
            try {
                table.takeForks(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eat();
            table.giveForks(id);
        }
    }

    private void think() {
        try {
//            System.out.println("THINKING " + id);
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void eat() {
        try {
//            System.out.println("EATING " + id);
            Thread.sleep(10);
            this.forkCount ++;
            Stats.add(id, forkCount);
//            System.out.println("END EATING " + id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void interrupt(){
        keepWorking = false;
    }
}
