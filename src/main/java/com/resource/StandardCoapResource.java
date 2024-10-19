package com.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.example.CoapDataManagerProcess;
import com.example.ResourceTypes;
import com.example.ResourceTypesManager;
import com.google.gson.Gson;
import com.utils.CoreInterfaces;

public class StandardCoapResource extends CoapResource {

    private String deviceId;
    private CoapDataManagerProcess process;
    protected Gson gson;

    public String getDeviceId() {
        return deviceId;
    }

    protected StandardCoapResource getInstance(ResourceTypes type) {
        return process.getInstance(type);

    }

    public StandardCoapResource(CoapDataManagerProcess dataManager, String name, String deviceId, ResourceTypes type) {
        super(name);
        process = dataManager;
        this.deviceId = deviceId;
        gson = new Gson();

        getAttributes().addAttribute("rt", ResourceTypesManager.getMap().get(type));
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString((MediaTypeRegistry.APPLICATION_SENML_JSON)));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

}
