package com.example;

import org.eclipse.californium.core.CoapServer;
import com.resource.AlarmControllerResource;
import com.resource.AlarmSwitchResource;
import com.resource.InfixSensorResource;
import com.resource.TouchBiometricSensorResource;
import com.utils.Log;

public class CoapDataManagerProcess extends CoapServer {

    AlarmControllerResource alarmController;
    AlarmSwitchResource alarmSwitch;
    InfixSensorResource infixSensor;
    TouchBiometricSensorResource touchBiometricSensor;

    public CoapDataManagerProcess() {
        super();
        String deviceId = "alarm-001";

        AlarmControllerResource alarmController = new AlarmControllerResource(AlarmControllerResource.getDefaultName(),
                deviceId);
        AlarmSwitchResource alarmSwitch = new AlarmSwitchResource(AlarmSwitchResource.getDefaultName(), deviceId);
        InfixSensorResource infixSensor = new InfixSensorResource(InfixSensorResource.getDefaultName(), deviceId);
        TouchBiometricSensorResource touchBiometricSensor = new TouchBiometricSensorResource(
                TouchBiometricSensorResource.getDefaultName(), deviceId);

        this.add(alarmController);
        this.add(alarmSwitch);
        this.add(infixSensor);
        this.add(touchBiometricSensor);
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
