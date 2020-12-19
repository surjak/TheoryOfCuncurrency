package com.surjak.lab8.v1;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

/**
 * Created by surjak on 19.12.2020
 */
public class Producer implements CSProcess {

    private One2OneChannelInt[] out;
    private One2OneChannelInt[] buffer;
    private int howMany;

    public Producer(final One2OneChannelInt[] out, final One2OneChannelInt[] buffer, final int howMany) {
        this.out = out;
        this.buffer = buffer;
        this.howMany = howMany;
    }

    public void run() {
        final Guard[] guards = new Guard[buffer.length];

        for (int i = 0; i < out.length; i++)
            guards[i] = buffer[i].in();

        final Alternative alt = new Alternative(guards);

        for (int i = 0; i < howMany; i++) {
            int index = alt.select();
            buffer[index].in().read();

            int item = (int) (Math.random() * 100) + 1;
            out[index].out().write(item);
        }
    }

}
