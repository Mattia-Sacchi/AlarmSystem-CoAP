package com.example;

import org.eclipse.californium.core.CoapServer;

import com.resource.CapsulaResource;
import com.resource.CoffeActuatorResource;
import com.resource.TemperatureResource;

public class CoapCoffeMachineProcess extends CoapServer {
    public CoapCoffeMachineProcess()
    {
        super();
        String deviceId = "coffe-machine-001";

        this.add(new TemperatureResource("temperature",deviceId));
        this.add(new CapsulaResource("capsule",deviceId));
        this.add(new CoffeActuatorResource("coffee",deviceId));

    }

    public static void main(String[] args) throws Exception {

        CoapCoffeMachineProcess coapServer = new CoapCoffeMachineProcess();
        coapServer.start();
        coapServer.getRoot().getChildren().forEach( resource->{
        System.out.println(
            String.format("Resource %s -> URI: %s ( Observable: %b )",
            resource.getName(), resource.getURI(), resource.isObservable()));
        });

    }
}
