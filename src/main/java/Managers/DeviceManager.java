package Managers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Descriptors.DeviceDescriptor;

public class DeviceManager {
    
    private HashMap<String,DeviceDescriptor> m_map;
    
    public DeviceManager()
    {
        m_map = new HashMap<>();
    }

    public List < DeviceDescriptor > getDeviceList () {
        return new ArrayList<>(m_map.values());
    }       

    public DeviceDescriptor getDevice(String deviceId) {
        return m_map.get(deviceId);
    }
    

    public DeviceDescriptor createNewDevice(DeviceDescriptor deviceDescriptor){
        if( deviceDescriptor == null ||
            getDevice(deviceDescriptor.getId()) != null )
        return null;
        m_map.put( deviceDescriptor.getId(), deviceDescriptor );
        return deviceDescriptor;
    }

    public DeviceDescriptor updateDevice ( DeviceDescriptor deviceDescriptor){
         m_map.put(deviceDescriptor.getId(), deviceDescriptor);
         return deviceDescriptor;
    }

    public DeviceDescriptor deleteDevice( String deviceId){
         return m_map.remove(deviceId );
    }



}
