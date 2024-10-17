package com.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.objects.AlarmController;
import com.utils.CoreInterfaces;

public class AlarmControllerResource  extends CoapResource {
    Gson gson;
    private static final String OBJECT_TITLE = "AlarmController";
    AlarmController controller;
    private String deviceId;


    public AlarmControllerResource(String name, String deviceId) {
        super(name);
        getAttributes().setTitle(OBJECT_TITLE);
        gson = new Gson();

        this.deviceId = deviceId;

        // Init
        getAttributes().addAttribute("rt", "com.resource.CaspulaResource");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString((MediaTypeRegistry.APPLICATION_SENML_JSON)));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        try {
            controller.setState(exchange.getRequestPayload().toString().equals("1"));
            exchange.respond(ResponseCode.CHANGED, new String(), MediaTypeRegistry.APPLICATION_JSON);
            changed();
        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }


}
