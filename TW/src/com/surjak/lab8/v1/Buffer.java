package com.surjak.lab8.v1;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannelInt;

/**
 * Created by surjak on 19.12.2020
 */
public class Buffer implements CSProcess {
    private One2OneChannelInt prod, cons, jeszcze;

    public Buffer(final One2OneChannelInt prod,
                  final One2OneChannelInt cons,
                  final One2OneChannelInt jeszcze) {
        this.cons = cons;
        this.prod = prod;
        this.jeszcze = jeszcze;
    }

    public void run() {
        while (true) {
            jeszcze.out().write(0);
            cons.out().write(prod.in().read());
        }
    }
}
