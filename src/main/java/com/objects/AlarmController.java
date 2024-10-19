package com.objects;

import com.utils.Log;

public class AlarmController {
    boolean state;

    public static final long EnterDelay = 30;
    public static final long ExitDelay = 30;

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

}
