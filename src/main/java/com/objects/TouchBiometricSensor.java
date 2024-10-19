package com.objects;

import java.util.ArrayList;

import com.utils.Log;

public class TouchBiometricSensor {

    ArrayList<String> validFingerPrints;

    public TouchBiometricSensor() {
        validFingerPrints = new ArrayList<String>();
    }

    // if there's no fingerprints, print error and return
    private boolean checkValidity() {
        boolean result = validFingerPrints.isEmpty();
        if (result)
            Log.error("Operation Failed", "Touch Biometric Sensor, not Initialized");
        return result;

    }

    public boolean addFingerPrint(String bioData) {
        if (validFingerPrints.contains(bioData)) {
            Log.error("Failed to add finger print:", bioData, "Already present in database");
            return false;
        }

        validFingerPrints.add(bioData);
        return true;
    }

    public boolean removeFingerPrint(String bioData) {
        if (checkValidity())
            return false;
        if (!validFingerPrints.contains(bioData)) {
            Log.error("Failed to remove finger print:", bioData, "Not present in database");
            return false;
        }
        return false;
    }

    public boolean checkBiometricData(String bioData) {
        if (checkValidity())
            return false;
        return validFingerPrints.contains(bioData);

    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

}
