package com.surjak.lab4;

import java.util.Objects;

/**
 * Created by surja on 27.10.2020
 */
public class CSVRow {
    public int bufferSize;
    public String producerOrConsumer;
    public int size;
    public int PK_config;
    public boolean isFair;
    public boolean randomization;
    public int counter;

    public CSVRow(int bufferSize, String producerOrConsumer, int size, int PK_config, boolean isFair, boolean randomization, int counter) {
        this.bufferSize = bufferSize;
        this.producerOrConsumer = producerOrConsumer;
        this.size = size;
        this.PK_config = PK_config;
        this.isFair = isFair;
        this.randomization = randomization;
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "CSVRow{" +
                "bufferSize=" + bufferSize +
                ", producerOrConsumer='" + producerOrConsumer + '\'' +
                ", size=" + size +
                ", PK_config=" + PK_config +
                ", isFair=" + isFair +
                ", randomization=" + randomization +
                ", counter=" + counter +
                '}';
    }
}
