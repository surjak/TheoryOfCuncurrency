package com.surjak.lab4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by surja on 27.10.2020
 */
public class BufferNaive implements IBuffer {
    private volatile int buffer = 0;
    public final int maxBufferSize;

    private Lock lock = new ReentrantLock();
    private Condition readCondition = lock.newCondition();
    private Condition writeCondition = lock.newCondition();


    public BufferNaive(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    public void put(int size) {
        lock.lock();
        while (this.buffer + size > maxBufferSize) {
            try {
                writeCondition.await();
            } catch (InterruptedException e) {
                lock.unlock();
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.buffer += size;
        readCondition.signalAll();
        lock.unlock();

    }

    public void pop(int size) {
        lock.lock();
        while (this.buffer - size < 0) {
            try {
                readCondition.await();
            } catch (InterruptedException e) {
                lock.unlock();
                Thread.currentThread().interrupt();
                return;
            }
        }
        this.buffer -= size;
        writeCondition.signal();
        lock.unlock();
    }
}
