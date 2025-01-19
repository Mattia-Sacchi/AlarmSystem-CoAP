package com.objects;
import java.util.Random;

public class TouchBiometricSensor {

    Random random;
    
    public TouchBiometricSensor() {
        random = new Random();
    }

    public static String getValidFingerPrint() {
        // Unique valid fingerprint
        return "SystemOwnerFingerprint00";
    }

    
    /*
    // Before the system was working with a ArrayList of fingerprints
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
    }*/

    public boolean checkBiometricData(String bioData) {
        return getValidFingerPrint().equals(bioData);

    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

    // Only for simulation purpose
    public String measure() {

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 10; i++){
            sb.append(Character.toChars(random.nextInt(97, 122)));
        }
        return sb.toString();
    }

}
