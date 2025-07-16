#include <SoftwareSerial.h>

#define LED_PIN 3

// Create a SoftwareSerial object using pins 10 (RX) and 11 (TX)
SoftwareSerial btSerial(10, 11); // RX, TX

void setup() {
  Serial.begin(9600);         // For debugging on Serial Monitor (USB)
  btSerial.begin(9600);       // For communication with HC-05
  pinMode(LED_PIN, OUTPUT);
  Serial.println("Ready");
}

void loop() {
  if (btSerial.available()) {
    char cmd = btSerial.read();
    Serial.print("Received: ");
    Serial.println(cmd);  // Debug to Serial Monitor

    switch(cmd) {
      case 'L': analogWrite(LED_PIN, 85); break;
      case 'M': analogWrite(LED_PIN, 170); break;
      case 'H': analogWrite(LED_PIN, 255); break;
      case 'O': analogWrite(LED_PIN, 0); break;
    }
  }
}
