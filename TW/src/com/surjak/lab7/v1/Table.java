package com.surjak.lab7.v1;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by surjak on 04.12.2020
 */
public class Table {

    private final Lock lock = new ReentrantLock();
    private List<Condition> conditions;
    private List<Philosopher> philosophers;
    private Map<Integer, Integer> availableForksPerPhilosopher;
    private final int numOfPhilosophers;

    public Table(int numOfPhilosophers) {
        this.numOfPhilosophers = numOfPhilosophers;
        this.conditions = IntStream
                .range(0, numOfPhilosophers)
                .boxed()
                .map(integer -> lock.newCondition())
                .collect(Collectors.toList());
        this.philosophers = IntStream
                .range(0, numOfPhilosophers)
                .boxed()
                .map(integer -> new Philosopher(integer, this))
                .collect(Collectors.toList());

        this.availableForksPerPhilosopher = philosophers.stream()
                .collect(Collectors.toMap(Philosopher::getId, philosopher -> 2));
    }

    public void run() throws InterruptedException {
        List<Thread> threads = philosophers.stream().map(Thread::new).collect(Collectors.toList());
        threads.forEach(Thread::start);
    }

    public void takeForks(int id) {
        lock.lock();
        while (availableForksPerPhilosopher.get(id) != 2) {
            try {
                conditions.get(id).await();
            } catch (InterruptedException e) {
                lock.unlock();
                return;
            }
        }
        Integer leftId = (id + 1) % numOfPhilosophers;
        Integer leftAvailableForks = availableForksPerPhilosopher.get(leftId);

        Integer rightId = id - 1 < 0 ? numOfPhilosophers - 1 : id - 1;
        Integer rightAvailableForks = availableForksPerPhilosopher.get(rightId);

        availableForksPerPhilosopher.put(leftId, leftAvailableForks - 1);
        availableForksPerPhilosopher.put(rightId, rightAvailableForks - 1);

        lock.unlock();
    }

    public void giveForks(int id) {
        lock.lock();
        Integer leftId = (id + 1) % numOfPhilosophers;
        Integer leftAvailableForks = availableForksPerPhilosopher.get(leftId);

        Integer rightId = id - 1 < 0 ? numOfPhilosophers - 1 : id - 1;

        Integer rightAvailableForks = availableForksPerPhilosopher.get(rightId);

        availableForksPerPhilosopher.put(leftId, leftAvailableForks + 1);
        availableForksPerPhilosopher.put(rightId, rightAvailableForks + 1);

        conditions.get(leftId).signal();
        conditions.get(rightId).signal();

        lock.unlock();
    }

    public void interrupt(){
        philosophers.forEach(philosopher -> philosopher.interrupt());
    }

}
