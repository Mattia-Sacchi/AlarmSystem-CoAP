package com.client;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.LinkFormat;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.server.resources.ResourceAttributes;
import org.eclipse.californium.elements.exception.ConnectorException;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
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
    public static final String INVALID_FINGERPRINT = "";
    private static int intrusionCount = 0;

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

        } catch (IOException | ConnectorException e) {

            return false;
        }
    }

    /*private static boolean createFingerprint(CoapClient client, String fingerprint) {
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
            record.setT(System.currentTimeMillis());
            pack.add(record);

            // Setting payload with additional check
            String payload = gson.toJson(pack);

            if (payload.equals(gson.toJson(JsonNull.INSTANCE))) {
                Log.error("Json Encoding failed");
                return false;
            }
            request.setPayload(payload);

            CoapResponse response = client.advanced(request);

            return (response != null &&
                    response.getCode().equals(CoAP.ResponseCode.CREATED));
        } catch (IOException | ConnectorException e) {
            return false;
        }
    }*/

    private static boolean setAlarm(CoapClient client, boolean state) {
        try {
            Request request = new Request(Code.PUT);
            request.setURI(composeUriDefault(ResourceTypes.RT_ALARM_SWITCH));
            request.setPayload(String.format(state ? "1" : "0"));
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);
            System.out.println(response.getCode());

            return (response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (IOException | ConnectorException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean setSiren(CoapClient client, boolean state) {
        try {
            Request request = new Request(Code.PUT);
            request.setURI(composeUriDefault(ResourceTypes.RT_ALARM_CONTROLLER));
            request.setPayload(String.format(state ? "1" : "0"));
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);
            System.out.println(response.getCode());

            return (response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (IOException | ConnectorException e) {
            System.out.println(e.getMessage());
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
            record.setT(System.currentTimeMillis());
            pack.add(record);

            // Setting payload with additional check
            String payload = gson.toJson(pack);

            if (payload.equals(gson.toJson(JsonNull.INSTANCE))) {
                Log.error("Json Encoding failed");
                return false;
            }
            request.setPayload(payload);

            CoapResponse response = client.advanced(request);

            return (response != null &&
                    response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (IOException | ConnectorException e) {
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

            if (pack.size() != 1)
                return INVALID_FINGERPRINT;

            return pack.get(0).getVs();
        } catch (JsonSyntaxException | IOException | ConnectorException e) {
            return INVALID_FINGERPRINT;
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

            if (pack.size() != 1)
                return false;
            return pack.get(0).getVb();
        } catch (JsonSyntaxException | IOException | ConnectorException e) {
            return false;
        }
    }

    public static void testWrongFingerprint(CoapClient client)
    {
        Log.operationResult(checkFingerprint(client, "WrongFingerprint"), "checking fingerprint");
        Log.debug("GET Fingerprint " ,getFingerprint(client) );
    }

    public static void testIntrusions(CoapClient client, int times)
    {
        for(int i = 0 ; i < times; i++)
            Log.operationResult(getInfixSensorValue(client), String.format("Infix sensor test intrusion %d", ++intrusionCount));
        
        
    }

    public static void testValidFingerprint(CoapClient client, String message)
    {
        Log.operationResult(checkFingerprint(client, "SystemOwnerFingerprint00"), message);
    }



    public static void main(String[] args) throws Exception {
        CoapClient client = new CoapClient();

        Log.operationResult(validateTargetDevice(client), "Client Validation");

        setAlarm(client, true);
        setSiren(client, true);

        setAlarm(client, false);
        setSiren(client, false);
        if(true)
            return;

        testWrongFingerprint(client);

        // Checking that the user doesn't try to turn on the system multiple times.

        testValidFingerprint(client, "checking fingerprint first time");
        Thread.sleep(1000);
        testValidFingerprint(client, "checking fingerprint second time");



        // I wait for the system to turn on in order to test a intrusion.
        Thread.sleep(5000);

        testIntrusions(client,4);
        
        
        // Test if I can stop the alarm with the fingerprint after a intrusion, before the siren goes on.
        Thread.sleep(1000);
        testValidFingerprint(client, "checking fingerprint third time");


        // Test the intrusion with the system offs
        testIntrusions(client,4);


        // Test a real intrusion
        testValidFingerprint(client, "checking fingerprint fourth time");

        Thread.sleep(6000);
        testIntrusions(client, 4);


        // Test if I can stop the intrusion with a fingerprint while the system is active
        Thread.sleep(6000);
        testValidFingerprint(client, "checking fingerprint fifth time");

        // Testing actuators manually

    }

}