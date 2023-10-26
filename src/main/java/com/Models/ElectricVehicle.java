package com.Models;

import java.util.Random;

public class ElectricVehicle extends Vehicle {
    GeoLocation m_location;
    double m_batteryLevel = 100.f;
    double m_km;
    double m_speed;
    double m_engineTemperature;
    long m_timestamp;
    private transient Random m_random;

    public ElectricVehicle(String uuid, String manifacturer, String model, String driverId)
    { 
        super(uuid,manifacturer,model,driverId);
        updateMeasurements(); 
    }
    public ElectricVehicle(Vehicle vehicle)
    { 
        super(vehicle.getUuid(),vehicle.getMunifacturer(),vehicle.getModel(),vehicle.getDriverId());
        updateMeasurements(); 
    }

    public void updateMeasurements () {

        if( m_random == null )
            m_random = new Random( System.currentTimeMillis() );

        double randomLatitude = 10.0 + m_random.nextDouble() * 10.0;
        double randomLongitude = 40.0 + m_random.nextDouble() * 10.0;

        m_location = new GeoLocation( randomLatitude , randomLongitude , 0.0) ;
        m_engineTemperature = 80 + m_random.nextDouble() * 20.0;
        m_batteryLevel = m_batteryLevel - ( m_random.nextDouble() * 5.0) ;
        m_speed = 10 + m_random.nextDouble() * 80.0;
        m_timestamp = System.currentTimeMillis();
    }

    
    public long getTimestamp()
    {
        return m_timestamp;
    }
    
    public void setTimestamp(long timestamp)
    {
        m_timestamp = timestamp;
    }
    
    public double getEngineTemperature()
    {
        return m_engineTemperature;
    }
    
    public void setEngineTemperature(double engineTemperature)
    {
        m_engineTemperature = engineTemperature;
    }
    
    public double getSpeed()
    {
        return m_speed;
    }
    
    public void setSpeed(double speed)
    {
        m_speed = speed;
    }
    
    public double getKm()
    {
        return m_km;
    }
    
    public void setKm(double km)
    {
        m_km = km;
    }
    
    public double getBatteryLevel()
    {
        return m_batteryLevel;
    }
    
    public void setBatteryLevel(double batteryLevel)
    {
        m_batteryLevel = batteryLevel;
    }
    
    public GeoLocation getLocation()
    {
        return m_location;
    }
    
    public void setLocation(GeoLocation location)
    {
        m_location = location;
    }
}
