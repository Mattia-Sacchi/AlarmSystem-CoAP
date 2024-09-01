package com.objects;

import java.util.Random;

public class InfixSensor {

    boolean state;
    Random random;

    public InfixSensor() {
        random = new Random();
        state = false;
    }

    public void measure() {
        this.state = random.nextBoolean();
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

}
