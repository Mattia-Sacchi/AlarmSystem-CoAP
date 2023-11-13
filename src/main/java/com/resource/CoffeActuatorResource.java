package com.resource;
import java.util.Random;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.objects.CoffeMachine;

public class CoffeActuatorResource  extends CoapResource{
    private Gson gson;
    private static final String OBJECT_TITLE = "CoffeActuator";
    CoffeMachine temp;

    public CoffeActuatorResource(String name)
    {
        super(name);
        getAttributes().setTitle(OBJECT_TITLE);
        setObservable(true);
        setObserveType(Type.CON);
        gson = new Gson();
        temp  = new CoffeMachine();

    }

    @Override
    public void handleGET(CoapExchange exchange)
    {
        try {
            String message = gson.toJson(temp);
            exchange.respond(ResponseCode.CONTENT, message, MediaTypeRegistry.APPLICATION_JSON);
        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePOST(CoapExchange exchange)
    {
        try {
            Random random = new Random();
            temp.increaseCoffe(random.nextInt(8));
            
            String message = gson.toJson(temp);
            exchange.respond(ResponseCode.CHANGED, message, MediaTypeRegistry.APPLICATION_JSON);
            changed();
        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePUT(CoapExchange exchange)
    {
        try {
            String payload = new String(exchange.getRequestPayload());
            temp.increaseCoffe(payload);
            exchange.respond(ResponseCode.CHANGED);
            changed();
        } catch (Exception e) {
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}


