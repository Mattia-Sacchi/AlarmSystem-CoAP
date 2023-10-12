package com.service;

import com.Descriptors.DeviceDescriptor;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;

public class AppService extends Application<AppConfig>{

    @Override
    public void run(AppConfig arg0, Environment arg1) throws Exception {
        addDemoDevice(arg0);
    }

    public void addDemoDevice(AppConfig config)
    {
        DeviceDescriptor descriptor = new DeviceDescriptor(
            "device00001","iot:demosensor", "acme-Inc", "v0.0.0.1");
        config.getDeviceManager().createNewDevice(descriptor);
    }

    
}
