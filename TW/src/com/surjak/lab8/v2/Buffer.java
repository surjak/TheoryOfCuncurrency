package com.surjak.lab8.v2;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

/**
 * Created by surjak on 19.12.2020
 */
public class Buffer implements CSProcess {
    private One2OneChannelInt in, out;

    public Buffer(final One2OneChannelInt in,
                  final One2OneChannelInt out) {
        this.out = out;
        this.in = in;
    }

    public void run() {
        while (true)
            out.out().write(in.in().read());
    }
}
