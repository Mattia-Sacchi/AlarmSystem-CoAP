package com.resource;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.objects.Caspula;

public class CapsulaResource extends CoapResource{
    private Gson gson;
    private static final String OBJECT_TITLE = "CapsulaSensor";
    Caspula temp;

    public CapsulaResource( String name)
    {
        super(name);
        getAttributes().setTitle(OBJECT_TITLE);
        gson = new Gson();
        temp  = new Caspula();

    }

    @Override
    public void handleGET(CoapExchange exchange)
    {
        try {
            temp.isThere();
            String message = gson.toJson(temp);
            exchange.respond(ResponseCode.CONTENT, message, MediaTypeRegistry.APPLICATION_JSON);
        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}


