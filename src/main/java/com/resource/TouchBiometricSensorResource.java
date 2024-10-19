package com.resource;

import java.util.Optional;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.objects.TouchBiometricSensor;
import com.utils.CoreInterfaces;
import com.utils.Log;
import com.utils.SenMLPack;
import com.utils.SenMLRecord;

public class TouchBiometricSensorResource extends CoapResource {

    Gson gson;
    private static final String OBJECT_TITLE = "TouchBiometricSensor";
    TouchBiometricSensor sensor;
    private String deviceId;

    public TouchBiometricSensorResource(String name, String deviceId) {
        super(name);
        gson = new Gson();
        getAttributes().setTitle(OBJECT_TITLE);
        sensor = new TouchBiometricSensor();
        this.deviceId = deviceId;

        // Init
        getAttributes().addAttribute("rt", "com.resource.TouchBiometricSensor");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString((MediaTypeRegistry.APPLICATION_SENML_JSON)));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

    }

    private Optional<String> getJsonSenMlResponse() {
        try {

            SenMLPack pack = new SenMLPack();
            SenMLRecord record = new SenMLRecord();
            record.setBn(deviceId);
            record.setN(getName());
            pack.add(record);
            return Optional.of(this.gson.toJson(pack));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void handlePOST(CoapExchange exchange) {

        try {
            SenMLPack senMLPack = gson.fromJson(exchange.getRequestText(), SenMLPack.class);

            // First condition it needs to have at least one record
            boolean result = !senMLPack.isEmpty();
            // Check if it has already the fingerprints if so it fails
            for (SenMLRecord r : senMLPack) {
                if (!sensor.addFingerPrint(r.getVs()))
                    result = false;
            }

            if (!result)
                exchange.respond(ResponseCode.BAD_REQUEST);

            exchange.respond(ResponseCode.CREATED, new String(), MediaTypeRegistry.APPLICATION_JSON);
            changed();

        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePUT(CoapExchange exchange) {
        try {
            SenMLPack senMLPack = gson.fromJson(exchange.getRequestText(), SenMLPack.class);

            // First condition the size must be one (One check at a time)
            if (senMLPack.size() != 1) {
                exchange.respond(ResponseCode.BAD_REQUEST);
                return;
            }

            exchange.respond(ResponseCode.CHANGED, new String(), MediaTypeRegistry.APPLICATION_JSON);
            changed();

        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        try {
            String payload = exchange.toString();
            Log.debug(payload);

            if (!(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON
                    || exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON)) {
                exchange.respond(CoAP.ResponseCode.CONTENT, String.format("%b", sensor.checkBiometricData(payload)),
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
