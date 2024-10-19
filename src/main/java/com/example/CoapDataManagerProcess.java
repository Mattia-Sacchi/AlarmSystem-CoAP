package com.example;

import org.eclipse.californium.core.CoapServer;
import com.resource.AlarmControllerResource;
import com.resource.AlarmSwitchResource;
import com.resource.InfixSensorResource;
import com.resource.TouchBiometricSensorResource;
import com.utils.Log;

public class CoapDataManagerProcess extends CoapServer {

    public CoapDataManagerProcess() {
        super();
        String deviceId = "alarm-001";

        AlarmControllerResource alarmControllerResource = new AlarmControllerResource("alarm-controller", deviceId);
        AlarmSwitchResource alarmSwitchResource = new AlarmSwitchResource("alarm-switch", deviceId);
        InfixSensorResource infixSensorResource = new InfixSensorResource("infix-sensor", deviceId);
        TouchBiometricSensorResource touchBiometricSensorResource = new TouchBiometricSensorResource(
                "touch-biometric-sensor", deviceId);
        this.add(alarmControllerResource);
        this.add(alarmSwitchResource);
        this.add(infixSensorResource);
        this.add(touchBiometricSensorResource);
    }

    public static void main(String[] args) throws Exception {
        CoapDataManagerProcess coapServer = new CoapDataManagerProcess();
        coapServer.start();
        coapServer.getRoot().getChildren().forEach(resource -> {
            Log.debug(
                    String.format("Resource %s -> URI: %s ( Observable: %b )",
                            resource.getName(), resource.getURI(), resource.isObservable()));
        });

    }
}
