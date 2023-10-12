package service;

import Managers.DeviceManager;
import io.dropwizard.core.Configuration;

public class AppConfig extends Configuration {

    private DeviceManager m_deviceManager = null;

    public DeviceManager getDeviceManager(){
        if(m_deviceManager == null)
            m_deviceManager = new DeviceManager();
        return m_deviceManager;
    }

}
