package com.server.manager;

import org.eclipse.californium.core.CoapServer;

import com.server.resource.AlarmControllerResource;
import com.server.resource.AlarmSwitchResource;
import com.server.resource.InfixSensorResource;
import com.server.resource.StandardCoapResource;
import com.server.resource.TouchBiometricSensorResource;
import com.utils.Log;
import com.utils.ResourceTypes;

public class CoapDataManagerProcess extends CoapServer {

    AlarmControllerResource alarmController;
    AlarmSwitchResource alarmSwitch;
    InfixSensorResource infixSensor;
    TouchBiometricSensorResource touchBiometricSensor;

    public CoapDataManagerProcess() {
        super();
        String deviceId = "alarm-001";

        alarmController = new AlarmControllerResource(this,
                deviceId, ResourceTypes.RT_ALARM_CONTROLLER);
        alarmSwitch = new AlarmSwitchResource(this, deviceId,
                ResourceTypes.RT_ALARM_SWITCH);
        infixSensor = new InfixSensorResource(this, deviceId,
                ResourceTypes.RT_INFIX_SENSOR);
        touchBiometricSensor = new TouchBiometricSensorResource(this,
                deviceId, ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR);

        this.add(alarmController);
        this.add(alarmSwitch);
        this.add(infixSensor);
        this.add(touchBiometricSensor);
    }

    public StandardCoapResource getInstance(ResourceTypes type) {

        switch (type) {
            case RT_ALARM_CONTROLLER:
                return alarmController;
            case RT_ALARM_SWITCH:
                return alarmSwitch;
            case RT_INFIX_SENSOR:
                return infixSensor;
            case RT_TOUCH_BIOMETRIC_SENSOR:
                return touchBiometricSensor;

            default:
                return new StandardCoapResource(null, null, null);
        }

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
