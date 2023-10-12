package com.example;
import com.Descriptors.DeviceDescriptor;
import com.service.AppService;


public class Main {
    public static void main(String[] args) throws Exception {
        new AppService().run(new String[]{"server","configuration.yaml"});
    }
}