package com.resource;

import java.util.Optional;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.example.CoapDataManagerProcess;
import com.example.ResourceTypes;
import com.google.gson.Gson;
import com.objects.InfixSensor;
import com.utils.CoreInterfaces;
import com.utils.SenMLPack;
import com.utils.SenMLRecord;

public class InfixSensorResource extends StandardCoapResource {

    private static final String OBJECT_TITLE = "InfixSensor";
    InfixSensor sensor;

    public static String getDefaultName() {
        return "infix-sensor";
    }

    public InfixSensorResource(CoapDataManagerProcess dataManager, String deviceId, ResourceTypes type) {
        super(dataManager, deviceId, type);
        getAttributes().setTitle(OBJECT_TITLE);
        sensor = new InfixSensor();

    }

    private Optional<String> getJsonSenMlResponse() {
        try {

            SenMLPack pack = new SenMLPack();
            SenMLRecord record = new SenMLRecord();
            record.setBn(getDeviceId());
            record.setN(getName());
            pack.add(record);
            return Optional.of(this.gson.toJson(pack));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        try {

            if (!(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON
                    || exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON)) {
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(sensor.getState()),
                        MediaTypeRegistry.TEXT_PLAIN);
                return;
            }

            Optional<String> senMlPayload = getJsonSenMlResponse();
            if (senMlPayload.isPresent())
                exchange.respond(CoAP.ResponseCode.CONTENT, senMlPayload.get(),
                        MediaTypeRegistry.APPLICATION_SENML_JSON);
            else
                exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);

        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

}
