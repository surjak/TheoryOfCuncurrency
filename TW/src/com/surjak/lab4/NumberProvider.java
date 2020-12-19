package com.surjak.lab4;

/**
 * Created by surja on 27.10.2020
 */
public class NumberProvider {
    private static Integer number = 0;

    public static Integer getNumber() {
        return number++;
    }
}
