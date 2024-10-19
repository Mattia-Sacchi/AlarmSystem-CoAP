package com.resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.example.CoapDataManagerProcess;
import com.example.ResourceTypes;
import com.objects.AlarmController;

public class AlarmControllerResource extends StandardCoapResource {
    private static final String OBJECT_TITLE = "AlarmController";
    private static AlarmController controller;

    public AlarmController getControllerInstance() {
        return controller;
    }

    public AlarmControllerResource(CoapDataManagerProcess dataManager, String deviceId, ResourceTypes type) {
        super(dataManager, deviceId, type);
        getAttributes().setTitle(OBJECT_TITLE);
        controller = new AlarmController();
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
