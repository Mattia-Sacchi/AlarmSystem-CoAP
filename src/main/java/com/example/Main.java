package com.example;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.Config.MqttConfigurationParameters;
import com.Models.ElectricVehicle;
import com.Models.Vehicle;
import com.Process.MqttVehicleProcessEmulator;
import com.google.gson.Gson;

public class Main {
    private static final int MESSAGE_COUNT = 1000;

    private static boolean verifyConnection(IMqttClient mqttClient)
    {
        boolean ret;
        if (ret = !mqttClient.isConnected())
            System.err.println(" Error : Topic or Msg = Null or MQTT Client is not Connected !");
        return ret;
    }

    public static void publishDeviceInfo(IMqttClient mqttClient, Vehicle vehicleDescriptor)
    {
        try {
            
            if (verifyConnection(mqttClient))
                return;
            
            Gson gson = new Gson();
            String topic = String.format("%s/%s/%s/%s",
            MqttConfigurationParameters.MQTT_BASIC_TOPIC ,
            MqttConfigurationParameters.VEHICLE_TOPIC ,
            vehicleDescriptor.getUuid() ,
            MqttConfigurationParameters.VEHICLE_INFO_TOPIC);

            String payloadString = gson.toJson(vehicleDescriptor);
            MqttMessage msg = new MqttMessage(payloadString.getBytes()) ;

            msg.setQos(0);
            msg.setRetained(true);
            mqttClient.publish(topic, msg);
            System.out.println(" Device Data Correctly Published ! Topic : " + topic + " Payload: " + payloadString );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void publishTelemetryData(IMqttClient mqttClient, ElectricVehicle vehicleDescriptor)
    {
        try {
            
            if (!mqttClient.isConnected())
                return;
            
            Gson gson = new Gson();
            String topic = String.format("%s/%s/%s/%s",
            MqttConfigurationParameters.MQTT_BASIC_TOPIC ,
            MqttConfigurationParameters.VEHICLE_TOPIC ,
            vehicleDescriptor.getUuid() ,
            MqttConfigurationParameters.VEHICLE_TELEMETRY_TOPIC);

            String payloadString = gson.toJson(vehicleDescriptor);
            MqttMessage msg = new MqttMessage(payloadString.getBytes()) ;

            if ( payloadString == null || topic == null )
                return; 
            
            msg.setQos(0) ;
            msg.setRetained(false);
            mqttClient.publish(topic, msg);
            System.out.println(" Device Data Correctly Published ! Topic : " + topic + " Payload: " + payloadString );
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        try{
            String vehicleId = String.format("vehicle-%s", MqttConfigurationParameters.MQTT_USERNAME);
            MqttClientPersistence persistence = new MemoryPersistence();
            IMqttClient mqttClient = new MqttClient(
            String.format("tcp://%s:%d", MqttConfigurationParameters.BROKER_ADDRESS,
                MqttConfigurationParameters.BROKER_PORT),vehicleId,persistence);
            MqttConnectOptions options = new MqttConnectOptions() ;
            options.setUserName(MqttConfigurationParameters.MQTT_USERNAME);
            options.setPassword(new String ( MqttConfigurationParameters.MQTT_PASSWORD).toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);           
            mqttClient.connect(options);           
            System.out.println("Connected !");            
            Vehicle vehicleDescriptor = new Vehicle(vehicleId, "Hennesey", "Venom F5",MqttConfigurationParameters.MQTT_USERNAME);     
            ElectricVehicle electricVehicleTelemetryData = new ElectricVehicle(vehicleDescriptor);
            publishDeviceInfo(mqttClient, vehicleDescriptor);

            for ( int i = 0; i < MESSAGE_COUNT ; i ++) {
                electricVehicleTelemetryData.updateMeasurements();
                publishTelemetryData(mqttClient, electricVehicleTelemetryData);
                Thread.sleep(3000);
            }

            mqttClient.disconnect();
            mqttClient.close();
            System.out.println("Disconnected !");
        }catch(Exception e){
            e.printStackTrace();
        }

    }   
}