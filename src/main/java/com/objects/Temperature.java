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

    public double getValue() 
    {
        return temperature;
    }
    public long getTimestamp() 
    {
        return timestamp;
    }

    public void measure()
    {
        temperature = ( random.nextDouble() * 10 ) + TEMPERATURE_MIN;
        timestamp = System.currentTimeMillis();
    }
    
}
