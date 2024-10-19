package com.example;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ResourceTypes {
    RT_ALARM_CONTROLLER,
    RT_ALARM_SWITCH,
    RT_INFIX_SENSOR,
    RT_TOUCH_BIOMETRIC_SENSOR;

};

public Map<ResourceTypes, String> getResouceTypesMap() {
     return Stream.of(
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_CONTROLLER, "com.resource.AlarmController"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_SWITCH, "com.resource.AlarmSwitch"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_INFIX_SENSOR, "com.resource.InfixSensor"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR, "com.resource.TouchBiometricSensor"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
}
