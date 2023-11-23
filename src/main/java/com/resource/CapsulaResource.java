package com.resource;
import java.util.Optional;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.objects.Caspula;
import com.utils.CoreInterfaces;
import com.utils.SenMLPack;
import com.utils.SenMLRecord;

public class CapsulaResource extends CoapResource{
    private Gson gson;
    private static final String OBJECT_TITLE = "CapsulaSensor";
    Caspula temp;
    private static final Number SENSOR_VERSION = 0.1;
    private String deviceId;

    public CapsulaResource( String name, String deviceId)
    {
        super(name);
        getAttributes().setTitle(OBJECT_TITLE);
        gson = new Gson();
        temp  = new Caspula();
        this.deviceId = deviceId;

        // Init 
        getAttributes().addAttribute("rt","com.resource.CaspulaResource");
        getAttributes().addAttribute("if",CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct",Integer.toString((MediaTypeRegistry.APPLICATION_SENML_JSON)));
        getAttributes().addAttribute("ct",Integer.toString(MediaTypeRegistry.TEXT_PLAIN));


    }

    private Optional<String> getJsonSenMlResponse()
    {
        try {

            SenMLPack pack = new SenMLPack();
            SenMLRecord record = new SenMLRecord();
            record.setBn(deviceId); 
            record.setN(getName());
            record.setVb(temp.getValue());
            record.setT(temp.getTimestamp());
            record.setBv(SENSOR_VERSION);
            pack.add(record);
            return Optional.of(this.gson.toJson(pack));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange)
    {
        try {
            temp.isThere();
            
            if(!(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON
            || exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON))
            {
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(temp.getValue()),MediaTypeRegistry.TEXT_PLAIN);
                return;
            }

            Optional<String> senMlPayload = getJsonSenMlResponse();
            if(senMlPayload.isPresent())
                exchange.respond(CoAP.ResponseCode.CONTENT, senMlPayload.get(),MediaTypeRegistry.APPLICATION_SENML_JSON);
            else
                exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            
        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}


