package com.surjak.lab3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by surja on 20.10.2020
 */
public class Buffer {
    private volatile int buffer = 0;
    private final int maxBufferSize;

    private Lock lock = new ReentrantLock();
    public Condition readCondition = lock.newCondition();
    public Condition writeCondition = lock.newCondition();


    public Buffer(int bufferSize) {
        this.maxBufferSize = bufferSize;
    }

    public void put(int size) {
        lock.lock();
        while (this.buffer + size > maxBufferSize && !Thread.currentThread().isInterrupted()) {
            try {
                if(Thread.currentThread().isInterrupted()){
                    lock.unlock();
                    return;}
                writeCondition.await();
                if(Thread.currentThread().isInterrupted()){
                    lock.unlock();

                    return;}

            } catch (InterruptedException e) {
                e.printStackTrace();
                writeCondition.signalAll();
                lock.unlock();
                Thread.currentThread().interrupt();
                return;
            }
        }
        if(Thread.currentThread().isInterrupted()){
            lock.unlock();

            return;}
        this.buffer += size;
        readCondition.signalAll();
        System.out.println("Put " + size + ": Buffer size " + this.buffer);
        lock.unlock();

    }

    public void pop(int size) {
        lock.lock();
        while (this.buffer - size < 0 && !Thread.currentThread().isInterrupted()) {
            try {

                if(Thread.currentThread().isInterrupted()){
                    lock.unlock();

                    return;}
                readCondition.await();
                if(Thread.currentThread().isInterrupted()) {
                    lock.unlock();
                    return;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                readCondition.signalAll();
                lock.unlock();

                Thread.currentThread().interrupt();
                return;
            }
        }
        if(Thread.currentThread().isInterrupted()) {
            lock.unlock();

            return;
        }
        this.buffer -= size;
        writeCondition.signal();
        System.out.println("Pop " + size + ": Buffer size " + this.buffer);

        lock.unlock();
    }

    public int getBufferSize() {
        return this.buffer;
    }
}
