package com.resources;
import org.glassfish.jersey.message.internal.MediaTypes;
import java.util.List;
import com.service.AppConfig;
import com.Descriptors.DeviceDescriptor;
import com.Managers.DeviceManager;

import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;



@Path("api/iot/inventory/device")
public class DeviceResource {

    private AppConfig m_config = null;
    
    public DeviceResource(AppConfig config)
    {
        m_config = config;
    }

    

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDevices(@Context ContainerRequestContext req)
    {

        try {
            System.out.println("Loading...");
            List<DeviceDescriptor> deviceList = 
                m_config.getDeviceManager().getDeviceList();

            if(deviceList.isEmpty())
                m_config.genericNotFoundError();

            return Response.ok(deviceList).build();
        } catch (Exception e) {
            e.printStackTrace();
            return m_config.genericInternalError();

        }
    }

        
    @GET
    @Path("/{device_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDeviceById(@Context ContainerRequestContext req, @PathParam("device_id") String id)
    {
        try {
            System.out.println("Loading...");

            DeviceManager deviceManager = m_config.getDeviceManager();
            DeviceDescriptor device = deviceManager.getDevice(id);

            if(device == null)
                return m_config.genericNotFoundError();

            return Response.ok(device).build();
        } catch (Exception e) {
            e.printStackTrace();
            return m_config.genericInternalError();

        }
        
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDevice(@Context ContainerRequestContext req
        ,@Context UriInfo uriInfo, DeviceDescriptor desc)
    {
        try {
            System.out.println("Loading...");

            if(desc == null || desc.getId() == null || desc.getId().length() == 0)
                return m_config.genericNotFoundError();

            DeviceManager deviceManager = m_config.getDeviceManager();

            

            return Response.ok(desc).build();
        } catch (Exception e) {
            e.printStackTrace();
            return m_config.genericInternalError();

        }
        
    }

    
    
}