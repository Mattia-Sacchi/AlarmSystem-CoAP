package com.resource;
import java.util.Optional;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.objects.Temperature;
import com.utils.CoreInterfaces;
import com.utils.SenMLPack;
import com.utils.SenMLRecord;

import javafx.scene.media.Media;
import com.utils.SenMLPack;

public class TemperatureResource extends CoapResource{
    private Gson gson;
    private static final String OBJECT_TITLE = "TemperatureSensor";
    private Temperature temp;
    private static final Number SENSOR_VERSION = 0.1;
    private String UNIT = "Cel";
    private String deviceId;

    public TemperatureResource( String name, String deviceId)
    {
        super(name);
        getAttributes().setTitle(OBJECT_TITLE);
        gson = new Gson();
        temp  = new Temperature();
        this.deviceId = deviceId;
        
        // Init

        getAttributes().addAttribute("rt","com.resource.TemperatureResource");
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
            record.setU(UNIT);
            record.setV(this.temp.getValue());
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
            temp.measure();
            
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
