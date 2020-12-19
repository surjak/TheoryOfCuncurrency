package com.surjak.lab8.v2;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;
import org.jcsp.lang.Parallel;
import org.jcsp.lang.StandardChannelIntFactory;

/**
 * Created by surjak on 19.12.2020
 */
public class Main {
    static final int nBuffers = 10;
    static final int nItems = 10000;

    public static void main(String[] args) {
        runOneTask();
    }

    public static void runOneTask() {
        StandardChannelIntFactory fact = new StandardChannelIntFactory();

        final One2OneChannelInt[] channels = fact.createOne2One(nBuffers + 1);

        CSProcess[] procList = new CSProcess[nBuffers + 2];

        procList[0] = new Producer(channels[0], nItems);
        procList[1] = new Consumer(channels[nBuffers], nItems);

        for (int i = 0; i < nBuffers; i++)
            procList[i + 2] = new Buffer(channels[i], channels[i + 1]);

        Parallel par = new Parallel(procList);
        par.run();
    }
}
