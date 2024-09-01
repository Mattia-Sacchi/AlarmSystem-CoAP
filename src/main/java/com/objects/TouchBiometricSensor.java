package com.objects;

public class TouchBiometricSensor {

    String alphaNumericData;

    TouchBiometricSensor() {
        alphaNumericData = "Lorem Ipsum";
    }

    void setBiometricData(String biometricData) {
        alphaNumericData = biometricData;
    }

    String getBiometricData() {
        return alphaNumericData;
    }

}
