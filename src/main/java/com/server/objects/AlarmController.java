package com.server.objects;

import com.utils.Log;

public class AlarmController {
    private boolean state;

    public AlarmController() {
        state = false;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean newState) {
        state = newState;
        Log.debug("Siren", state ? "Intrusion detected (On)" : "Off");
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

}
