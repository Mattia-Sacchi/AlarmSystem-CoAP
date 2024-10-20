package com.objects;

import com.utils.Log;

public class AlarmSwitch {
    public static final long ENTER_DELAY = 5;
    public static final long EXIT_DELAY = 5;
    private boolean state;

    public AlarmSwitch() {
        state = false;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean newState) {
        state = newState;
        Log.debug("Alarm system", state ? "Armed (On)" : "Disarmed (Off)");
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

}
