package com.service;

import io.dropwizard.Configuration;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.Managers.DeviceManager;


public class AppConfig extends Configuration {

    private DeviceManager m_deviceManager = null;


    public Response genericError(Response.StatusType status,String message)
    {
        return Response.status(status)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new ErrorMessage(
                status.getStatusCode(),message))
                .build();
    }

    public DeviceManager getDeviceManager(){
        if(m_deviceManager == null)
            m_deviceManager = new DeviceManager();
        return m_deviceManager;
    }

}
