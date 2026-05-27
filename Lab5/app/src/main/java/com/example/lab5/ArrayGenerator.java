package com.example.lab5;

import java.util.Random;

public class ArrayGenerator {
    public static int[] generateArray(int size) {

        Random random = new Random();
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(100);
        }

        return arr;
    }
}
