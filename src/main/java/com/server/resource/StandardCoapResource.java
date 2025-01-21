package com.server.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.server.manager.CoapDataManagerProcess;
import com.google.gson.Gson;
import com.utils.CoreInterfaces;
import com.utils.ResourceTypes;
import com.utils.ResourceTypesManager;

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

    public StandardCoapResource(CoapDataManagerProcess dataManager, String deviceId, ResourceTypes type) {
        super(ResourceTypesManager.getDF().get(type));
        process = dataManager;
        this.deviceId = deviceId;
        gson = new Gson();

        getAttributes().addAttribute("rt", ResourceTypesManager.getRT().get(type));
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString((MediaTypeRegistry.APPLICATION_SENML_JSON)));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

}
