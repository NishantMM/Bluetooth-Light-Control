# Bluetooth LED Strip Controller 🔦📱

A DIY project to wirelessly control the brightness of an LED strip using an Android app and Arduino Nano over Bluetooth.

## 🔧 Features
- 3 brightness levels: LOW, MEDIUM, HIGH
- OFF button
- Bluetooth connection via HC-05
- Jetpack Compose Android app
- Battery or external power options

## 🧰 Hardware Used
- Arduino Nano (FTDI or CH340)
- HC-05 Bluetooth module
- TIP122 NPN Transistor
- 330Ω resistor
- LED strip (1W, 5V or 9V)
- Power supply or 9V battery
- Breadboard & jumper wires

## 📱 App
Built in Jetpack Compose (Android Studio - Kotlin). Connects to HC-05 and sends brightness control characters (`L`, `M`, `H`, `O`) to Arduino.

## 🧠 Arduino
Uses `SoftwareSerial` on pins D10 (Rx) and D11 (Tx) to communicate with HC-05 and drives PWM on D3 to control brightness.

## 📷 Diagrams
- Breadboard connection diagram
- Circuit schematic

## 📂 Project Structure
- `Arduino_Code/`: Contains `.ino` file
- `Android_App/`: Android Studio source code
- `Schematics/`: Wiring diagrams

## 📝 License
MIT License
