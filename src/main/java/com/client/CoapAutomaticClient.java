package com.client;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.server.resources.ResourceAttributes;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.utils.Log;
import com.utils.SenMLPack;
import com.utils.SenMLRecord;

public class CoapAutomaticClient {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5683";
    private static final String RESOURCE_DISCOVERY_ENDPOINT = "/.well-known/core";

    enum ResourceTypes {
        RT_ALARM_CONTROLLER,
        RT_ALARM_SWITCH,
        RT_INFIX_SENSOR,
        RT_TOUCH_BIOMETRIC_SENSOR,

    };

    private static final Map<ResourceTypes, String> resourceTypes = Stream.of(
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_CONTROLLER, "com.resource.AlarmController"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_SWITCH, "com.resource.AlarmSwitch"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_INFIX_SENSOR, "com.resource.InfixSensor"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR, "com.resource.TouchBiometricSensor"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    private static final Map<ResourceTypes, String> uris = Stream.of(
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_CONTROLLER, ""),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_SWITCH, ""),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_INFIX_SENSOR, ""),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR, ""))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    private static String testerDeviceId = "tester-device-000";
    private static String testerDeviceName = "CoapAutomaticClient";
    private static Gson gson = new Gson();

    private static String composeUriDefault(ResourceTypes type) {
        return composeUri(COAP_ENDPOINT, type);
    }

    private static String composeUri(String endpoint, ResourceTypes type) {
        return composeUri(endpoint, uris.get(type));
    }

    private static String composeUri(String endpoint, String uri) {
        return String.format("%s%s", COAP_ENDPOINT, uri);
    }

    private static boolean validateTargetDevice(CoapClient client) {
        try {
            Request request = new Request(Code.GET);
            request.setURI(composeUri(COAP_ENDPOINT, RESOURCE_DISCOVERY_ENDPOINT));
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);

            if (response == null) {
                Log.error("No response", String.format("No response from %s", COAP_ENDPOINT));
                return false;
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

                            for (Map.Entry<ResourceTypes, String> it : resourceTypes.entrySet()) {
                                if (value.equals(it.getValue()))
                                    uris.put(it.getKey(), uri);
                            }

                        });
            });

            // Check if any of the url is invalid -> return false
            boolean result = true;
            for (Map.Entry<ResourceTypes, String> it : uris.entrySet()) {
                if (it.getValue().isEmpty())
                    return false; // No need to continue
            }

            return result;

        } catch (Exception e) {

            return false;
        }
    }

    private static boolean createFingerprint(CoapClient client, String fingerprint) {
        try {
            Request request = new Request(Code.POST);
            request.setURI(composeUriDefault(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR));
            request.setConfirmable(true);

            // Making the formal request with SenML
            SenMLPack pack = new SenMLPack();
            SenMLRecord record = new SenMLRecord();
            record.setBn(testerDeviceId);
            record.setN(testerDeviceName);
            record.setVs(fingerprint);
            pack.add(record);

            // Setting payload with additional check
            String payload = gson.toJson(pack);

            if (payload == gson.toJson(JsonNull.INSTANCE)) {
                Log.error("Json Encoding failed");
                return false;
            }
            request.setPayload(payload);

            CoapResponse response = client.advanced(request);

            return (response != null &&
                    response.getCode().equals(CoAP.ResponseCode.CREATED));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkFingerprint(CoapClient client, String fingerprint) {
        try {
            Request request = new Request(Code.PUT);
            request.setURI(composeUriDefault(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR));
            request.setConfirmable(true);

            // Making the formal request with SenML
            SenMLPack pack = new SenMLPack();
            SenMLRecord record = new SenMLRecord();
            record.setBn(testerDeviceId);
            record.setN(testerDeviceName);
            record.setVs(fingerprint);
            pack.add(record);

            // Setting payload with additional check
            String payload = gson.toJson(pack);

            if (payload == gson.toJson(JsonNull.INSTANCE)) {
                Log.error("Json Encoding failed");
                return false;
            }
            request.setPayload(payload);

            CoapResponse response = client.advanced(request);

            return (response != null &&
                    response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        CoapClient client = new CoapClient();

        Log.operationResult(validateTargetDevice(client), "Client Validation");

        Log.operationResult(createFingerprint(client, "aa"), "test");

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