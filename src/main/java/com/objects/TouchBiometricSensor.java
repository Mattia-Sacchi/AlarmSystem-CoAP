package com.objects;

public class TouchBiometricSensor {

    String alphaNumericData;

    public TouchBiometricSensor() {
        alphaNumericData = "Lorem Ipsum";
    }

    public void setBiometricData(String biometricData) {
        alphaNumericData = biometricData;
    }

    public String getBiometricData() {
        return alphaNumericData;
    }

    public long getTimestamp()
    {
        return System.currentTimeMillis();
    }

}
