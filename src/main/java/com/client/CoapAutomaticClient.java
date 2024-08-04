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
import com.utils.SenMLPack;

public class CoapAutomaticClient {
    private static final String COAP_ENDPOINT = "coap://127.0.0.1:5683";
    private static final String RESOURCE_DISCOVERY_ENDPOINT = "/.well-known/core";
    private static String RT_TEMPERATURE_SENSOR = "com.resource.TemperatureResource";
    private static String RT_CASPULA_SENSOR = "com.resource.CaspulaResource";
    private static String RT_ACTUATOR_SENSOR = "com.resource.ActuatorResource";
    private static String targetTemperatureUri = null;
    private static String targetCaspulaUri = null;
    private static String targetActuatorUri = null;
    private static Gson gson = new Gson();

    private static boolean validateTargetDevice(CoapClient client) {
        try {
            Request request = new Request(Code.GET);
            request.setURI(String.format("%s%s", COAP_ENDPOINT, RESOURCE_DISCOVERY_ENDPOINT));
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);

            if (response == null) {
                System.out.println("No risposta");
                return false;
            }

            if (response.getOptions().getContentFormat() != MediaTypeRegistry.APPLICATION_LINK_FORMAT) {
                System.out.println("Format diverso");
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

                            if (value.equals(RT_TEMPERATURE_SENSOR))
                                targetTemperatureUri = uri;
                            else if (value.equals(RT_CASPULA_SENSOR))
                                targetCaspulaUri = uri;
                            else if (value.equals(RT_ACTUATOR_SENSOR))
                                targetActuatorUri = uri;

                        });
            });

            return (targetActuatorUri != null && targetTemperatureUri != null && targetCaspulaUri != null);

        } catch (Exception e) {

            return false;
        }
    }

    private static boolean isCoffeAvaiable(CoapClient client) {
        try {
            Request request = new Request(Code.GET);
            request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));
            request.setURI(String.format(COAP_ENDPOINT, targetCaspulaUri));
            request.setConfirmable(true);
            CoapResponse response = client.advanced(request);

            if (response == null)
                return false;

            String payload = response.getResponseText();
            SenMLPack pack = gson.fromJson(payload, SenMLPack.class);
            return pack.get(0).getVb();
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean trigger(CoapClient client) {
        try {
            Request request = new Request(Code.POST);
            request.setURI(String.format(COAP_ENDPOINT, targetActuatorUri));
            request.setConfirmable(true);
            CoapResponse response = client.advanced(request);

            return (response != null && response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean createCaspula(CoapClient client) {
        try {
            Request request = new Request(Code.POST);
            request.setURI(String.format("%s%s", COAP_ENDPOINT, targetCaspulaUri));
            System.out.println(String.format("%s%s", COAP_ENDPOINT, targetCaspulaUri));
            request.setConfirmable(true);
            System.out.println(Utils.prettyPrint(request));
            CoapResponse response = client.advanced(request);
            System.out.println(Utils.prettyPrint(response));

            return (response != null && response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean light(CoapClient client, String color, boolean state) {
        try {
            Request request = new Request(Code.PUT);
            request.setURI(String.format("coap://192.168.4.1:5683/light/").concat(color));
            request.setPayload(state ? "1" : "0");
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);
            System.out.println(response.getCode());

            return (response != null && response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean light(CoapClient client) {
        try {
            Request request = new Request(Code.GET);
            request.setURI(String.format("coap://192.168.4.1:5683/light/red"));

            CoapResponse response = client.advanced(request);
            System.out.println(response.getCode());

            boolean result = (response != null && response.getCode().equals(CoAP.ResponseCode.CONTENT));
            System.out.println(response.toString());
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean trafficLight(CoapClient client) {
        try {
            Request request = new Request(Code.PUT);
            request.setURI(String.format("coap://192.168.4.1:5683/trafficlight/"));
            request.setPayload(String.format("{\"timings\":[10000,5000,6000],\"cmd\":2,\"freq\":350,\"which\":5}"));
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);
            System.out.println(response.getCode());

            return (response != null && response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static boolean trafficLight(CoapClient client, String payload) {
        try {
            Request request = new Request(Code.PUT);
            request.setURI(String.format("coap://192.168.4.1:5683/trafficlight/"));
            request.setPayload(String.format(payload));
            request.setConfirmable(true);

            CoapResponse response = client.advanced(request);
            System.out.println(response.getCode());

            return (response != null && response.getCode().equals(CoAP.ResponseCode.CHANGED));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        CoapClient client = new CoapClient();

        if (!trafficLight(client)) {
            System.out.println("Errore Client non valido");
            return;
        } else {
            System.out.println("Cambiato");
            return;

        }

    }
}
