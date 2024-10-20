package com.utils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceTypesManager {

    private static final Map<ResourceTypes, String> resourceTypes = Stream.of(
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_CONTROLLER, "com.resource.AlarmController"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_SWITCH, "com.resource.AlarmSwitch"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_INFIX_SENSOR, "com.resource.InfixSensor"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR, "com.resource.TouchBiometricSensor"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    private static final Map<ResourceTypes, String> defaultNames = Stream.of(
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_CONTROLLER, "alarm-controller"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_ALARM_SWITCH, "alarm-switch"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_INFIX_SENSOR, "infix-sensor"),
            new AbstractMap.SimpleEntry<>(ResourceTypes.RT_TOUCH_BIOMETRIC_SENSOR, "touch-biometric-sensor"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public static Map<ResourceTypes, String> getRT() {
        return resourceTypes;
    }

    public static Map<ResourceTypes, String> getDF() {
        return defaultNames;
    }

}
