package com.service;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import com.resources.DeviceResource;

import com.Descriptors.DeviceDescriptor;


public class AppService extends Application<AppConfig>{

    @Override
    public void run(AppConfig arg0, Environment arg1) throws Exception {
        addDemoDevice(arg0);
        arg1.jersey().register(new DeviceResource(arg0));
    }

    public void addDemoDevice(AppConfig config)
    {
        DeviceDescriptor descriptor = new DeviceDescriptor(
            "device00001","iot:demosensor", "acme-Inc", "v0.0.0.1");
        config.getDeviceManager().createNewDevice(descriptor);
    }

    
}
