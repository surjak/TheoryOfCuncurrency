package com.surjak.lab7.v2;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by surjak on 04.12.2020
 */
public class Table {
    private Semaphore arbiter;
    private List<Semaphore> forks;
    private int numberOfPhilosophers;
    private List<Philosopher> philosophers;

    public Table(int numberOfPhilosophers) {
        this.numberOfPhilosophers = numberOfPhilosophers;
        arbiter = new Semaphore(numberOfPhilosophers - 1);

        initForksWithSemaphores(numberOfPhilosophers);

        initPhilosophers(numberOfPhilosophers);
    }

    public void initForksWithSemaphores(int numberOfPhilosophers) {
        forks = IntStream.range(0, numberOfPhilosophers)
                .boxed()
                .map(integer -> new Semaphore(1))
                .collect(Collectors.toList());
    }

    public void initPhilosophers(int numberOfPhilosophers) {
        philosophers = IntStream.range(0, numberOfPhilosophers)
                .boxed()
                .map(integer -> new Philosopher(integer, this))
                .collect(Collectors.toList());
    }

    public void run() {
        this.philosophers.stream().map(Thread::new).forEach(Thread::start);
    }

    public void takeForks(int id) throws InterruptedException {
        arbiter.acquire();
        forks.get(id).acquire();
        forks.get((id + 1) % numberOfPhilosophers).acquire();
    }

    public void giveForks(int id) {
        forks.get(id).release();
        forks.get((id + 1) % numberOfPhilosophers).release();
        arbiter.release();
    }

    public void interrupt(){
        this.philosophers.forEach(Philosopher::interrupt);
    }
}
