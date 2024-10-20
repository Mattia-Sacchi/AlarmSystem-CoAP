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
import com.utils.Constants;
import com.utils.Log;
import com.utils.ResourceTypes;
import com.utils.ResourceTypesManager;
import com.utils.SenMLPack;
import com.utils.SenMLRecord;

public class CoapAutomaticClient {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5683";
    private static final String RESOURCE_DISCOVERY_ENDPOINT = "/.well-known/core";
    private static Gson gson = new Gson();
    private static String testerDeviceId = "tester-device-000";
    private static String testerDeviceName = "CoapAutomaticClient";

    private static final Map<ResourceTypes, String> uris = Stream.of(
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_CONTROLLER, ""),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_SWITCH, ""),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_INFIX_SENSOR, ""),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR, ""))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

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

                            for (Map.Entry<ResourceTypes, String> it : ResourceTypesManager.getRT().entrySet()) {
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

    private static String getFingerprint(CoapClient client) {
        try {
            Request request = new Request(Code.GET);
            request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));
            request.setURI(composeUriDefault(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR));
            request.setConfirmable(true);
            CoapResponse response = client.advanced(request);

            String payload = response.getResponseText();
            SenMLPack pack = gson.fromJson(payload, SenMLPack.class);

            if (response == null || pack.size() != 1)
                return Constants.INVALID_FINGERPRINT;

            return pack.get(0).getVs();
        } catch (Exception e) {
            return Constants.INVALID_FINGERPRINT;
        }
    }

    private static boolean getInfixSensorValue(CoapClient client) {
        try {
            Request request = new Request(Code.GET);
            request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));
            request.setURI(composeUriDefault(ResourceTypes.RT_INFIX_SENSOR));
            request.setConfirmable(true);
            CoapResponse response = client.advanced(request);

            String payload = response.getResponseText();
            SenMLPack pack = gson.fromJson(payload, SenMLPack.class);

            if (response == null || pack.size() != 1)
                return false;

            return pack.get(0).getVb();
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        CoapClient client = new CoapClient();

        Log.operationResult(validateTargetDevice(client), "Client Validation");

        Log.operationResult(createFingerprint(client, "aa"), "creating finger print");

        Log.operationResult(checkFingerprint(client, "aa"), "checking finger print");
        Thread.sleep(1000);

        Log.operationResult(getInfixSensorValue(client), "Infix sensor");

        Thread.sleep(5 * 1000);

        Log.operationResult(getInfixSensorValue(client), "Infix sensor");

    }

}