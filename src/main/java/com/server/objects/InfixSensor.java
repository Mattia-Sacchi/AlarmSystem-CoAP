package com.server.objects;
import java.util.Random;

public class InfixSensor {

    boolean state;
    Random random;

    public InfixSensor() {
        state = false;
        random = new Random();
    }

    public boolean getState() {
        return state;
    }

    // Only simulation
    public void setState(boolean newState) {
        state = newState;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    public void measure() {
        this.state = random.nextBoolean();
    }

}
