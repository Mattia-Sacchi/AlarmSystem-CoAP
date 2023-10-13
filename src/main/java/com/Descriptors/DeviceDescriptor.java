package com.Descriptors;

public class DeviceDescriptor {
    private String id;
    private String type;
    private String manufacturer;
    private String swVersion;

    public boolean equals(DeviceDescriptor  device)
    {
        return device.id == this.id;
    }

    public DeviceDescriptor(String id, String type, String manufacturer, String sw)
    {
        this.id = id;
        this.type = type;
        this.manufacturer = manufacturer;
        swVersion = sw;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getSwVersion()
    {
        return swVersion;
    }

    public void setSwVersion(String swVersion)
    {
        this.swVersion = swVersion;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

}
