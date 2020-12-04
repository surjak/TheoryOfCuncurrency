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
        initConditionList(numOfPhilosophers);
        initPhilosophers(numOfPhilosophers);
        initForksPerPhilosopher();
    }

    public void initForksPerPhilosopher() {
        this.availableForksPerPhilosopher = philosophers.stream()
                .collect(Collectors.toMap(Philosopher::getId, philosopher -> 2));
    }

    public void initPhilosophers(int numOfPhilosophers) {
        this.philosophers = IntStream
                .range(0, numOfPhilosophers)
                .boxed()
                .map(integer -> new Philosopher(integer, this))
                .collect(Collectors.toList());
    }

    public void initConditionList(int numOfPhilosophers) {
        this.conditions = IntStream
                .range(0, numOfPhilosophers)
                .boxed()
                .map(integer -> lock.newCondition())
                .collect(Collectors.toList());
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
        Integer leftId = getLeft(id);
        Integer rightId = getRight(id);

        decreaseLeftAndRight(leftId, rightId);

        lock.unlock();
    }

    public void decreaseLeftAndRight(Integer leftId, Integer rightId) {
        Integer leftAvailableForks = availableForksPerPhilosopher.get(leftId);
        Integer rightAvailableForks = availableForksPerPhilosopher.get(rightId);

        availableForksPerPhilosopher.put(leftId, leftAvailableForks - 1);
        availableForksPerPhilosopher.put(rightId, rightAvailableForks - 1);
    }

    public void giveForks(int id) {
        lock.lock();
        Integer leftId = getLeft(id);

        Integer rightId = getRight(id);

        increaseLeftAndRight(leftId, rightId);

        signalNeighbours(leftId, rightId);

        lock.unlock();
    }

    public void increaseLeftAndRight(Integer leftId, Integer rightId) {
        Integer leftAvailableForks = availableForksPerPhilosopher.get(leftId);
        Integer rightAvailableForks = availableForksPerPhilosopher.get(rightId);

        availableForksPerPhilosopher.put(leftId, leftAvailableForks + 1);
        availableForksPerPhilosopher.put(rightId, rightAvailableForks + 1);
    }

    public void signalNeighbours(Integer leftId, Integer rightId) {
        conditions.get(leftId).signal();
        conditions.get(rightId).signal();
    }

    public int getRight(int id) {
        return id - 1 < 0 ? numOfPhilosophers - 1 : id - 1;
    }

    public int getLeft(int id) {
        return (id + 1) % numOfPhilosophers;
    }

    public void interrupt(){
        philosophers.forEach(Philosopher::interrupt);
    }

}
