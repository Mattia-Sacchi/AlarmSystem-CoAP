package com.resources;
import org.glassfish.jersey.message.internal.MediaTypes;

import java.net.URI;
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
                return m_config.genericError(Response.Status.NOT_FOUND,"Device not Found");

            return Response.ok(deviceList).build();
        } catch (Exception e) {
            e.printStackTrace();
            return m_config.genericError(Response.Status.INTERNAL_SERVER_ERROR,"Internal server error");

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
                return m_config.genericError(Response.Status.NOT_FOUND,"Device not Found");

            return Response.ok(device).build();
        } catch (Exception e) {
            e.printStackTrace();
            return m_config.genericError(Response.Status.INTERNAL_SERVER_ERROR,"Internal server error");

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

            String id = desc.getId();

            if(desc == null || id == null || id.length() == 0)
                return m_config.genericError(Response.Status.BAD_REQUEST,"Check the request Caion!");

            DeviceManager deviceManager = m_config.getDeviceManager();
            
            if(deviceManager.getDevice(id) != null)
                return m_config.genericError(Response.Status.CONFLICT,"Device with the same UUID Exist");

            deviceManager.createNewDevice(desc);

            String requestPath = uriInfo.getAbsolutePath().toString();
            String locationHeaderString = String.format("%s/%s", requestPath, id);

            return Response.created(new URI(locationHeaderString)).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return m_config.genericError(Response.Status.INTERNAL_SERVER_ERROR,"Internal Server Error!");

        }
        
    }

    
    
}