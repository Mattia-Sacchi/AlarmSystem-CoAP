package com.resource;

import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.example.CoapDataManagerProcess;
import com.objects.AlarmSwitch;
import com.utils.ResourceTypes;

public class AlarmSwitchResource extends StandardCoapResource {
    private static final String OBJECT_TITLE = "AlarmSwitch";
    private static AlarmSwitch alarmSwitch;

    public AlarmSwitch getAlarmSwitchInstance() {
        return alarmSwitch;
    }

    public AlarmSwitchResource(CoapDataManagerProcess dataManager, String deviceId, ResourceTypes type) {
        super(dataManager, deviceId, type);
        getAttributes().setTitle(OBJECT_TITLE);
        alarmSwitch = new AlarmSwitch();
    }

    @Override
    public void handlePOST(CoapExchange exchange) {
        try {
            alarmSwitch.setState(exchange.getRequestPayload().toString().equals("1"));
            exchange.respond(ResponseCode.CHANGED, new String(), MediaTypeRegistry.APPLICATION_JSON);
            changed();
        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

}
