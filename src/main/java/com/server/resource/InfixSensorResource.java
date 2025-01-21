package com.server.resource;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.server.manager.CoapDataManagerProcess;
import com.server.objects.AlarmController;
import com.server.objects.AlarmSwitch;
import com.server.objects.InfixSensor;
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
        sensor.measure();
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

    void scheduledTask()
    {
        boolean actualSystemState = ((AlarmSwitchResource) getInstance(ResourceTypes.RT_ALARM_SWITCH))
                        .getInstance().getState();
        AlarmController siren = ((AlarmControllerResource) getInstance(
            ResourceTypes.RT_ALARM_CONTROLLER)).getInstance();
        if (actualSystemState) {
            siren.setState(true);
        }
    }

    private void valueChanged(boolean state) {
        // I get a alarm system resource Instance
        AlarmSwitchResource alarmSwitchRes = ((AlarmSwitchResource) getInstance(ResourceTypes.RT_ALARM_SWITCH));
        // I get a alarm siren resource Instance
        AlarmControllerResource alarmControllerRes = ((AlarmControllerResource) getInstance(
                ResourceTypes.RT_ALARM_CONTROLLER));

        // I get a alarm system Instance
        AlarmSwitch alarmSwitch = alarmSwitchRes.getInstance();
        // I get a alarm siren Instance
        AlarmController alarmController = alarmControllerRes.getInstance();

        // I get the current state of the systems
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

        if (alarmSystemState && !alarmSirenState) {
            Log.debug("Turning on the siren",
                    String.format("You have %d seconds to enter the fingerprint", AlarmSwitch.ENTER_DELAY));
            ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
            Runnable task = () -> scheduledTask();
            ses.schedule(task, AlarmSwitch.ENTER_DELAY, TimeUnit.SECONDS);

            ses.shutdown();
        }

    }

    /*
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

    */

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
