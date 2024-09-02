package com.resource;

import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.google.gson.Gson;
import com.utils.CoreInterfaces;

public class TouchBiometricSensorResource {
    public TouchBiometricSensorResource(String name, String deviceId) {
        super(name);
        getAttributes().setTitle(OBJECT_TITLE);
        gson = new Gson();

        this.deviceId = deviceId;

        // Init
        getAttributes().addAttribute("rt", "com.resource.TouchBiometricSensor");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString((MediaTypeRegistry.APPLICATION_SENML_JSON)));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

    }

}
