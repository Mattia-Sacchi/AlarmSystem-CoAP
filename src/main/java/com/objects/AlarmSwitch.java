package com.objects;

public class AlarmSwitch {
    private boolean state;
    private long timestamp;
    
    public AlarmSwitch()
    {
        state = false;
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
 
     public long getTimestamp() 
     {
         return timestamp = System.currentTimeMillis();
     }


}
