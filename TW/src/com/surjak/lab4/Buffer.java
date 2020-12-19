package com.surjak.lab4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by surja on 20.10.2020
 */
public class Buffer implements IBuffer {
    private volatile int buffer = 0;
    public final int maxBufferSize;

    private Lock lock = new ReentrantLock();

    private Lock consumerLock = new ReentrantLock(true);
    private Lock producerLock = new ReentrantLock(true);


    private Condition readCondition = lock.newCondition();
    private Condition writeCondition = lock.newCondition();


    public Buffer(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    public void put(int size) {
        producerLock.lock();
        lock.lock();
        while (this.buffer + size > maxBufferSize) {
            try {
                writeCondition.await();
            } catch (InterruptedException e) {
                lock.unlock();
                producerLock.unlock();
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.buffer += size;
        readCondition.signal();
        lock.unlock();
        producerLock.unlock();
    }

    public void pop(int size) {
        consumerLock.lock();
        lock.lock();
        while (this.buffer - size < 0) {
            try {
                readCondition.await();
            } catch (InterruptedException e) {
                lock.unlock();
                consumerLock.unlock();
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.buffer -= size;
        writeCondition.signal();
        lock.unlock();
        consumerLock.unlock();
    }
}
