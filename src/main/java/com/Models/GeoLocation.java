package com.Models;

public class GeoLocation {

    GeoLocation(double longitude, double latitude, double altitude)
    {
        m_longitude = longitude;
        m_latitude = latitude;
        m_altitude = altitude;
    }
    private double m_longitude = 0.f;
    private double m_latitude = 0.f;
    private double m_altitude = 0.f;


    public double getLongitude()
    {
        return m_longitude;
    }

    public double getLatitude()
    {
        return m_latitude;
    }

    public double getAltitude()
    {
        return m_altitude;
    }

    public void setLongitude(double longitude)
    {
        m_longitude = longitude;
    }

    public void setLatitude(double latitude)
    {
        m_latitude = latitude;
    }

    public void setAltitude(double altitude)
    {
        m_altitude = altitude;
    }


}
