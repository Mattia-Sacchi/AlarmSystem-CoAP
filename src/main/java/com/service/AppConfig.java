package com.service;

import io.dropwizard.Configuration;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.Managers.DeviceManager;


public class AppConfig extends Configuration {

    private DeviceManager m_deviceManager = null;

    public Response genericInternalError()
    {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new ErrorMessage(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error!"))
                .build();
    }

    public Response genericNotFoundError()
    {
        return Response.status(Response.Status.NOT_FOUND)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new ErrorMessage(
                Response.Status.NOT_FOUND.getStatusCode(),"No Devices found"))
                .build();
    }

    public Response genericBadRequestError()
    {
        return Response.status(Response.Status.NOT_FOUND)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new ErrorMessage(
                Response.Status.NOT_FOUND.getStatusCode(),"No Devices found"))
                .build();
    }

    public DeviceManager getDeviceManager(){
        if(m_deviceManager == null)
            m_deviceManager = new DeviceManager();
        return m_deviceManager;
    }

}
