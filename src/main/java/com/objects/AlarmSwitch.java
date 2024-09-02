package com.objects;

public class AlarmSwitch {
    private boolean state;

    public AlarmSwitch() {
        state = false;
    }

    public void setState(boolean newState) {
        state = newState;
    }

    public boolean getState() {
        return state;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

}
