# IIOT Demo Project (for University) 

## 1. Installation

Run the following command to clone the repository:
```
git clone git@github.com:Mattia-Sacchi/IIOT_Project.git
```

# 🏠 Smart Home Security System

## 📌 Project Overview
This project is an **Intelligent Internet of Things (IoT) Smart Home Alarm System** designed to detect intrusions using **window/door sensors** and trigger an **alarm siren** when unauthorized access is detected. The system also includes a **biometric sensor** for fingerprint recognition, which allows users to toggle the alarm state.

## 🎯 Features
- **Intrusion Detection**: Monitors door/window sensors for unauthorized access.
- **Smart Alarm Activation**: Triggers an alarm **30 seconds after detection** (giving the user time to disable it).
- **Biometric Authentication**: Uses a **Touch Biometric Sensor** to recognize fingerprints.
- **Automatic Alarm Control**:
  - If an authorized fingerprint is detected, the system:
    - **Turns off** the siren if active.
    - **Toggles** the alarm state (ON/OFF).
  - If the alarm is turned ON via fingerprint, a **30-second delay** is provided to exit the house safely.
- **Modular & Scalable**: Can be designed to support multiple sensors and devices.

## 🛠 Components
| Component             | Type       | Description |
|-----------------------|-----------|-------------|
| **Touch Biometric Sensor** | Sensor | Detects fingerprints and sends data to the system. |
| **Window/Door Sensor** | Sensor | Detects the state (open/closed) of doors and windows. |
| **Alarm Switch** | Actuator | Controls the alarm system (ON/OFF). |
| **Alarm Controller** | Smart Object | Manages the alarm siren (ON/OFF). |

## 📡 Data Flow & System Logic
1. **Data Collection**:
   - The **Window/Door Sensor** monitors door/window states.
   - The **Touch Biometric Sensor** captures fingerprint data.
   
2. **Alarm Activation**:
   - If an intrusion is detected while the alarm is ON, the **alarm siren activates after 30 seconds**.
   - If a recognized fingerprint is scanned:
     - The siren is turned **OFF** (if active).
     - The alarm system **toggles** between ON/OFF states.

3. **System Scalability**:
   - The project can be extended to support multiple sensors and devices.
   - The demo version will simulate a minimal number of devices to validate the system.

## 🚀 Getting Started
1. **Install Required Dependencies** (if applicable).
2. **Connect the Sensors and Actuators** to the system.
3. **Run the Application** and test the intrusion detection and biometric authentication.
4. **Monitor the Alarm State** and adjust settings as needed.

## 📧 Contact
- **Project Author**: Mattia Sacchi
- **Personal Email**: [mattiasacchi2002@gmail.com](mailto:mattiasacchi2002@gmail.com)
- **Uni Email**: [306499@studenti.unimore.it](mailto:306499@studenti.unimore.it)
