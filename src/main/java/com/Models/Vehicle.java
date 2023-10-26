package com.Models;

public class Vehicle {
    private String m_uuid = null;
    private String m_manifacturer = null;
    private String m_model = null;
    private String m_driverId = null;
    
    public Vehicle(String uuid, String manifacturer, String model, String driverId)
    {
        m_uuid = uuid;
        m_manifacturer = manifacturer;
        m_model = model;
        m_driverId = driverId;
    }

    
    public String getUuid()
    {
        return m_uuid;
    }
    
    public void setUuid(String uuid)
    {
        m_uuid = uuid;
    }
    
    public String getMunifacturer()
    {
        return m_manifacturer;
    }
    
    public void setManufacturer(String manifacturer)
    {
        m_manifacturer = manifacturer;
    }
    
    public String getModel()
    {
        return m_model;
    }
    
    public void setModel(String model)
    {
        m_model = model;
    }
    
    public String getDriverId()
    {
        return m_driverId;
    }
    
    public void setDriverId(String driverId)
    {
        m_driverId = driverId;
    }
}
