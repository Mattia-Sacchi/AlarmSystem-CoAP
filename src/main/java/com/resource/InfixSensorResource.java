package com.resource;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.example.CoapDataManagerProcess;
import com.objects.AlarmController;
import com.objects.AlarmSwitch;
import com.objects.InfixSensor;
import com.utils.Constants;
import com.utils.Log;
import com.utils.ResourceTypes;
import com.utils.SenMLPack;
import com.utils.SenMLRecord;

public class InfixSensorResource extends StandardCoapResource {

    private static final String OBJECT_TITLE = "InfixSensor";
    InfixSensor sensor;

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
            record.setVb(sensor.getState());
            record.setT(sensor.getTimestamp());
            record.setU("bit");
            pack.add(record);
            return Optional.of(this.gson.toJson(pack));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void valueChanged(boolean state) {
        // I take a alarm system resource Instance
        AlarmSwitchResource alarmSwitchRes = ((AlarmSwitchResource) getInstance(ResourceTypes.RT_ALARM_SWITCH));
        // I take a alarm siren resource Instance
        AlarmControllerResource alarmControllerRes = ((AlarmControllerResource) getInstance(
                ResourceTypes.RT_ALARM_CONTROLLER));

        // I take a alarm system Instance
        AlarmSwitch alarmSwitch = alarmSwitchRes.getAlarmSwitchInstance();
        // I take a alarm siren Instance
        AlarmController alarmController = alarmControllerRes.getControllerInstance();

        // I take the current state of the system
        boolean alarmSystemState = alarmSwitch.getState();
        boolean alarmSirenState = alarmController.getState();

        // Check for errors (implausibility)
        if (alarmSirenState && !alarmSystemState) {
            Log.error("Implausibility", "The system isn't armed while the siren is running?",
                    "Disarming the system");
            // Try to return to a acceptable situation
            alarmController.setState(false);
            return;
        }

        // Se il sistema d'allarme non é acceso non faccio niente
        if (!state || !alarmSystemState) {
            return;
        }

        if (alarmSystemState) {
            Log.debug("Turning on the siren",
                    String.format("You have %d seconds to enter the fingerprint", AlarmSwitch.ENTER_DELAY));
            ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
            Runnable task = () -> {
                // I take a alarm system resource Instance
                boolean actualSystemState = ((AlarmSwitchResource) getInstance(ResourceTypes.RT_ALARM_SWITCH))
                        .getAlarmSwitchInstance().getState();
                if (actualSystemState) {
                    alarmController.setState(true);
                }
            };
            ses.schedule(task, AlarmSwitch.ENTER_DELAY, TimeUnit.SECONDS);

            ses.shutdown();
        }
        return;

    }

    // Only simulation
    @Override
    public void handlePUT(CoapExchange exchange) {
        try {
            SenMLPack senMLPack = gson.fromJson(exchange.getRequestText(), SenMLPack.class);

            // First condition the size must be one (One check at a time)
            if (senMLPack.size() != 1) {
                exchange.respond(ResponseCode.BAD_REQUEST);
                return;
            }

            // I take the simulated value (only for debug)
            boolean newState = senMLPack.get(0).getVb();
            sensor.setState(newState);

            valueChanged(newState);
            exchange.respond(ResponseCode.CHANGED, new String(), MediaTypeRegistry.APPLICATION_JSON);
            changed();

        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {
        try {

            sensor.measure();

            if (!(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON
                    || exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON)) {
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(sensor.getState()),
                        MediaTypeRegistry.TEXT_PLAIN);
                return;
            }

            valueChanged(sensor.getState());

            Optional<String> senMlPayload = getJsonSenMlResponse();
            if (senMlPayload.isPresent()) {
                exchange.respond(CoAP.ResponseCode.CONTENT, senMlPayload.get(),
                        MediaTypeRegistry.APPLICATION_SENML_JSON);
            } else {
                exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

}
