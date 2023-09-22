package org.example.fist;

import java.util.Random;

public class EnergyGeneratorImpl implements EnergyGenerator {
    private final Random random = new Random();


    @Override
    public int[] generate(int n) {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = random.nextInt(10_000);
        }
        return result;
    }
}
