package org.example.util;

import java.util.Random;

public class RandomService {
    private static final Random random = new Random();
    public int randomInt(int bound) {
        return random.nextInt(bound);
    }
}
