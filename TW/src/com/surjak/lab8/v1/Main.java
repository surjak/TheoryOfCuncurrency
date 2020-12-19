package com.surjak.lab8.v1;

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
        StandardChannelIntFactory intFactory = new StandardChannelIntFactory();

        final One2OneChannelInt[] channels_prod = intFactory.createOne2One(nBuffers);
        final One2OneChannelInt[] channels_buffer = intFactory.createOne2One(nBuffers);
        final One2OneChannelInt[] channels_cons = intFactory.createOne2One(nBuffers);

        CSProcess[] procList = new CSProcess[nBuffers + 2];
        procList[0] = new Producer(channels_prod, channels_buffer, nItems);
        procList[1] = new Consumer(channels_cons, nItems);

        for (int i = 0; i < nBuffers; i++)
            procList[i + 2] = new Buffer(channels_prod[i], channels_cons[i], channels_buffer[i]);

        Parallel par = new Parallel(procList);
        par.run();

    }
}
