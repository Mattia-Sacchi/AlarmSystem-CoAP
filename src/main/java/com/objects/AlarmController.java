package com.objects;

public class AlarmController {
    boolean state;

    public AlarmController()
    {
        state = false;
    }

    public boolean getState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

}
