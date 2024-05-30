package com.objects;

import java.util.Random;

public class OpeningsSensor {
    private boolean state; // 0 closed 1 open
    private long timestamp;
    private Random random;

    
    public OpeningsSensor()
    {
        random = new Random();
    }

    // OFT
    public void setState( boolean newState)
    {
        state = newState;
    }

    public boolean getState() 
    {
        return state;
    }

    public void measure()
    {
        state = random.nextInt(100) > 90; // Low probability

    }

    public long getTimestamp() 
    {
        return timestamp = System.currentTimeMillis();
    }




}
