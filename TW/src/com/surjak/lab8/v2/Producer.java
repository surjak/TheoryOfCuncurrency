package com.surjak.lab8.v2;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

/**
 * Created by surjak on 19.12.2020
 */
public class Producer implements CSProcess {
    private One2OneChannelInt out;
    private int howMany;

    public Producer(final One2OneChannelInt out, final int howMany) {
        this.out = out;
        this.howMany = howMany;
    }

    public void run() {
        for (int i = 0; i < howMany; i++) {
            int item = (int) (Math.random() * 100) + 1;
            out.out().write(item);
        }
    }

}
