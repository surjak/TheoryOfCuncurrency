package com.surjak.lab7.v1;

/**
 * Created by surjak on 04.12.2020
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        Table table = new Table(5);
        table.run();
        Thread.sleep(5000);
        table.interrupt();
        Stats.getPhilosopherForkCount().forEach((integer, integer2) -> System.out.println(integer + " : " + integer2));
    }
}
