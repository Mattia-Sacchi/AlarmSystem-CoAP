package com.client;

import java.util.Set;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.server.resources.ResourceAttributes;

import com.google.gson.Gson;
import com.utils.Log;
import com.utils.SenMLPack;

public class CoapAutomaticClient {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5683";
    private static final String RESOURCE_DISCOVERY_ENDPOINT = "/.well-known/core";
    private static String RT_ALARM_CONTROLLER = "com.resource.AlarmController";
    private static String RT_ALARM_SWITCH = "com.resource.AlarmSwitch";
    private static String RT_INFIX_SENSOR = "com.resource.InfixSensor";
    private static String RT_TOUCH_BIOMETRIC_SENSOR = "com.resource.TouchBiometricSensor";
    private static String targetAlarmControllerUri = null;
    private static String targetAlarmSwitchUri = null;
    private static String targetInfixSensorUri = null;
    private static String targetTouchBiometricSensorUri = null;
    private static Gson gson = new Gson();

    private static boolean validateTargetDevice(CoapClient client) {
        try {
            Request request = new Request(Code.GET);
            request.setURI(String.format("%s%s", COAP_ENDPOINT, RESOURCE_DISCOVERY_ENDPOINT));
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);

            if (response == null) {
                Log.error("No response", String.format("No response from %s", COAP_ENDPOINT));
                // return false;
            }

            if (response.getOptions().getContentFormat() != MediaTypeRegistry.APPLICATION_LINK_FORMAT) {
                Log.error("Wrong format", "Format expected: Application Link");
                return false;
            }

            Set<WebLink> links = LinkFormat.parse(response.getResponseText());
            links.forEach(link -> {
                String uri = link.getURI();
                ResourceAttributes attributes = link.getAttributes();
                attributes.getAttributeKeySet().forEach(
                        key -> {
                            String value = attributes.getAttributeValues(key).get(0);

                            if (!key.equals("rt"))
                                return;

                            // Log.debug("Found Resource", value);

                            if (value.equals(RT_ALARM_CONTROLLER))
                                targetAlarmControllerUri = uri;
                            else if (value.equals(RT_ALARM_SWITCH))
                                targetAlarmSwitchUri = uri;
                            else if (value.equals(RT_INFIX_SENSOR))
                                targetInfixSensorUri = uri;
                            else if (value.equals(RT_TOUCH_BIOMETRIC_SENSOR))
                                targetTouchBiometricSensorUri = uri;

                        });
            });

            boolean result = true;
            result = result && (targetAlarmControllerUri != null);
            result = result && (targetAlarmSwitchUri != null);
            result = result && (targetInfixSensorUri != null);
            result = result && (targetTouchBiometricSensorUri != null);

            return result;

        } catch (Exception e) {

            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        CoapClient client = new CoapClient();

        Log.operationResult(validateTargetDevice(client), "Client Validation");

    }
}

/*
 * private static boolean isCoffeAvaiable(CoapClient client) {
 * try {
 * Request request = new Request(Code.GET);
 * request.setOptions(new
 * OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));
 * request.setURI(String.format(COAP_ENDPOINT, targetCaspulaUri));
 * request.setConfirmable(true);
 * CoapResponse response = client.advanced(request);
 * 
 * if (response == null)
 * return false;
 * 
 * String payload = response.getResponseText();
 * SenMLPack pack = gson.fromJson(payload, SenMLPack.class);
 * return pack.get(0).getVb();
 * } catch (Exception e) {
 * return false;
 * }
 * }
 * 
 * private static boolean trigger(CoapClient client) {
 * try {
 * Request request = new Request(Code.POST);
 * request.setURI(String.format(COAP_ENDPOINT, targetActuatorUri));
 * request.setConfirmable(true);
 * CoapResponse response = client.advanced(request);
 * 
 * return (response != null &&
 * response.getCode().equals(CoAP.ResponseCode.CHANGED));
 * } catch (Exception e) {
 * return false;
 * }
 * }
 * 
 * private static boolean createCaspula(CoapClient client) {
 * try {
 * Request request = new Request(Code.POST);
 * request.setURI(String.format("%s%s", COAP_ENDPOINT, targetCaspulaUri));
 * System.out.println(String.format("%s%s", COAP_ENDPOINT, targetCaspulaUri));
 * request.setConfirmable(true);
 * System.out.println(Utils.prettyPrint(request));
 * CoapResponse response = client.advanced(request);
 * System.out.println(Utils.prettyPrint(response));
 * 
 * return (response != null &&
 * response.getCode().equals(CoAP.ResponseCode.CHANGED));
 * } catch (Exception e) {
 * return false;
 * }
 * }
 * 
 * private static boolean light(CoapClient client, String color, boolean state)
 * {
 * try {
 * Request request = new Request(Code.PUT);
 * request.setURI(String.format("coap://192.168.4.1:5683/light/").concat(color))
 * ;
 * request.setPayload(state ? "1" : "0");
 * request.setConfirmable(true);
 * 
 * CoapResponse response = client.advanced(request);
 * System.out.println(response.getCode());
 * 
 * return (response != null &&
 * response.getCode().equals(CoAP.ResponseCode.CHANGED));
 * } catch (Exception e) {
 * System.out.println(e.getMessage());
 * return false;
 * }
 * }
 * 
 * private static boolean trafficLight(CoapClient client) {
 * try {
 * Request request = new Request(Code.PUT);
 * request.setURI(String.format("coap://192.168.4.1:5683/trafficlight/"));
 * request.setPayload(String.format(
 * "{\"timings\":[10000,5000,6000],\"cmd\":2,\"freq\":350,\"which\":5}"));
 * request.setConfirmable(true);
 * 
 * CoapResponse response = client.advanced(request);
 * System.out.println(response.getCode());
 * 
 * return (response != null &&
 * response.getCode().equals(CoAP.ResponseCode.CHANGED));
 * } catch (Exception e) {
 * System.out.println(e.getMessage());
 * return false;
 * }
 * }
 */