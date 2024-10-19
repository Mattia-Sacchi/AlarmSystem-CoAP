package com.objects;

import com.utils.Log;

public class AlarmSwitch {
    private boolean state;

    public AlarmSwitch() {
        state = false;
    }

    public void setState(boolean newState) {
        state = newState;
        Log.debug("Alarm system", state ? "Armed (On)" : "Disarmed (Off)");
    }

    public boolean getState() {
        return state;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

}
