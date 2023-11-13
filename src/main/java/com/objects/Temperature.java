package com.objects;

import java.util.Random;

public class Temperature {
    static final float TEMPERATURE_MIN = 25.f;
    public Temperature() 
    {
        random = new Random();
        measure();
    }

    private Random random;
    private long timestamp;
    private double temperature;

    public void measure()
    {
        temperature = ( random.nextDouble() * 10 ) + TEMPERATURE_MIN;
        timestamp = System.currentTimeMillis();
    }
    
}
