package com.server.resource;

import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.server.manager.CoapDataManagerProcess;
import com.server.objects.AlarmController;
import com.utils.ResourceTypes;

public class AlarmControllerResource extends StandardCoapResource {
    private static final String OBJECT_TITLE = "AlarmController";
    private static AlarmController controller;

    public AlarmController getInstance() {
        return controller;
    }

    public AlarmControllerResource(CoapDataManagerProcess dataManager, String deviceId, ResourceTypes type) {
        super(dataManager, deviceId, type);
        getAttributes().setTitle(OBJECT_TITLE);
        controller = new AlarmController();
    }

    @Override
    public void handlePUT(CoapExchange exchange) {
        try {
            String payload = exchange.getRequestText();

            switch (payload) {
                case "1":
                    controller.setState(true);
                    break;
                case "0":
                    controller.setState(false);
                    break;
                default:
                    exchange.respond(ResponseCode.BAD_REQUEST);
                    return;    
            }

            exchange.respond(ResponseCode.CHANGED, new String(), MediaTypeRegistry.APPLICATION_JSON);
            changed();
        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

}
