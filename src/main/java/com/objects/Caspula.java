package com.objects;

import java.util.Random;

public class Caspula {
    public Caspula()
    {
        random = new Random();
    }

    private boolean presentContinuous;
    private long timestamp;
    private Random random;


    public void setCapsul(boolean noCap)
    {   
        presentContinuous = noCap;
    }

    public boolean getValue() 
    {
        return presentContinuous;
    }
    
    public long getTimestamp() 
    {
        return timestamp;
    }


    public boolean isThere()
    {
        timestamp = System.currentTimeMillis();
        return presentContinuous = random.nextBoolean();
    }

}
