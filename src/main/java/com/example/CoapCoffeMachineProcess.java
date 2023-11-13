package com.example;

import org.eclipse.californium.core.CoapServer;

import com.resource.CapsulaResource;
import com.resource.CoffeActuatorResource;
import com.resource.TemperatureResource;

public class CoapCoffeMachineProcess extends CoapServer {
    public CoapCoffeMachineProcess()
    {
        super();

        this.add(new TemperatureResource("temperature"));
        this.add(new CapsulaResource("capsule"));
        this.add(new CoffeActuatorResource("cofee"));

    }

    public static void main(String[] args) throws Exception {

        CoapCoffeMachineProcess coapServer = new CoapCoffeMachineProcess();
        coapServer.start();
        coapServer.getRoot().getChildren().forEach( resource->{
        System.out.println(
            String.format("Resource %s -> URI: %s ( Observable: %b )", resource.getName(), resource.getURI(), resource.isObservable()));
        });

    }
}
