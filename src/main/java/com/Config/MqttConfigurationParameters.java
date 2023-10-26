package com.Config;

public class MqttConfigurationParameters {
    public static final String MQTT_USERNAME = "306499@studenti.unimore.it";
    public static final String MQTT_PASSWORD = "wgqhgzmhymzzbpve";
    public static final String MQTT_BASIC_TOPIC = String.format ("/iot/user/%s",MQTT_USERNAME);
    public static final String VEHICLE_TOPIC = "vehicle";
    public static final String VEHICLE_TELEMETRY_TOPIC = "telemetry";
    public static final String VEHICLE_INFO_TOPIC = "info";

    public static String BROKER_ADDRESS = "155.185.4.4";
    public static int BROKER_PORT = 7883;
}
