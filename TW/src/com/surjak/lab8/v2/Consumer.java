package com.surjak.lab8.v2;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

/**
 * Created by surjak on 19.12.2020
 */
public class Consumer implements CSProcess {
    private One2OneChannelInt in;
    private int howMany;

    public Consumer(final One2OneChannelInt in, final int howMany) {
        this.in = in;
        this.howMany = howMany;
    }

    public void run() {
        long start = System.nanoTime();
        for (int i = 0; i < howMany; i++) {
            int item = in.in().read();
//            System.out.println(item);
        }
        long end = System.nanoTime();
        System.out.println((end - start) + "ns");
        System.exit(0);
    }
}

