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

        this.add(new AlarmControllerResource("alarm-controller", deviceId));
        this.add(new AlarmSwitchResource("alarm-switch", deviceId));
        this.add(new InfixSensorResource("infix-sensor", deviceId));
        this.add(new TouchBiometricSensorResource("touch-biometric-sensor", deviceId));
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
