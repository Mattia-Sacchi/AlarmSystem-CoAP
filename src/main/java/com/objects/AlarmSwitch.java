package com.objects;

public class AlarmSwitch {
<<<<<<< HEAD

    boolean state;

    public AlarmSwitch() {
        state = false;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
=======
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

>>>>>>> origin/master

}
