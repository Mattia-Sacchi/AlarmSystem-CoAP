package com.resource;
<<<<<<< HEAD

public class AlarmSwitchResource {

}
=======
import java.util.Optional;
import java.util.Random;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.google.gson.Gson;
import com.objects.AlarmSwitch;
import com.utils.CoreInterfaces;
import com.utils.SenMLPack;
import com.utils.SenMLRecord;

public class AlarmSwitchResource  extends CoapResource{
    private Gson gson;
    private static final String OBJECT_TITLE = "CoffeActuator";
    AlarmSwitch temp;
    private static final Number SENSOR_VERSION = 0.1;
    private String UNIT = "count";
    private String deviceId;

    public AlarmSwitchResource(String name, String deviceId)
    {
        super(name);
        getAttributes().setTitle(OBJECT_TITLE);
        setObservable(true);
        setObserveType(Type.CON);
        gson = new Gson();
        temp  = new CoffeMachine();
        this.deviceId = deviceId;

        // Init 

        getAttributes().addAttribute("rt","com.resource.ActuatorResource");
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
            record.setBv(SENSOR_VERSION);
            record.setV(temp.getTotal());
            record.setT(System.currentTimeMillis());
            pack.add(record);

            for(int i = 0; i < CoffeMachine.CoffeTypes.CT_COUNT.getValue(); i++)
            {
                SenMLRecord rec = new SenMLRecord();
                final CoffeTypes type = temp.valueOf(i);
                rec.setN(type.getName());
                rec.setV(temp.getCount(type));
                pack.add(record);
            }
            return Optional.of(this.gson.toJson(pack));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange)
    {
        try {
            
            if(!(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON
            || exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON))
            {
                exchange.respond(CoAP.ResponseCode.CONTENT, temp.toString(),MediaTypeRegistry.TEXT_PLAIN);
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


>>>>>>> origin/master
