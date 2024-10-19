package com.resource;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.objects.AlarmController;
import com.objects.AlarmSwitch;
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

    public static String getDefaultName() {
        return "touch-biometric-sensor";
    }

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

            String fingerprint = senMLPack.get(0).getVs();

            if (!sensor.checkBiometricData(fingerprint)) {
                exchange.respond(ResponseCode.UNAUTHORIZED);
                return;
            }

            // I take a alarm system Instance
            AlarmSwitch alarmSwitch = ((AlarmSwitchResource) getChild(AlarmSwitchResource.getDefaultName()))
                    .getAlarmSwitchInstance();
            // I take a alarm siren Instance
            AlarmController alarmController = ((AlarmControllerResource) getChild(
                    AlarmControllerResource.getDefaultName()))
                    .getControllerInstance();

            // I take the current state of the system
            boolean alarmSystemState = alarmSwitch.getState();
            boolean alarmSirenState = alarmController.getState();

            // Check for errors (implausibility)
            if (alarmSirenState && !alarmSystemState) {
                Log.error("Implausibility", "The system isn't armed while the siren is running?",
                        "Disarming the system");
                // Try to return to a acceptable situation
                alarmController.setState(false);
                exchange.respond(ResponseCode.CHANGED, new String(), MediaTypeRegistry.APPLICATION_JSON);
                return;
            }

            if (alarmSirenState && alarmSystemState) {
                alarmController.setState(false);
                alarmSwitch.setState(false);
                exchange.respond(ResponseCode.CHANGED, new String(), MediaTypeRegistry.APPLICATION_JSON);
                return;
            }

            // Ok now the cases with delay needed
            if (!alarmSystemState) {
                Log.debug("Arming the system",
                        String.format("You have %d seconds to leave the house", AlarmController.ExitDelay));
                ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
                Runnable task = () -> {
                    alarmSwitch.setState(true);
                };
                ses.schedule(task, AlarmController.ExitDelay, TimeUnit.SECONDS);

                ses.shutdown();

            }

            exchange.respond(ResponseCode.CHANGED, new String(), MediaTypeRegistry.APPLICATION_JSON);
            changed();

        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

}
