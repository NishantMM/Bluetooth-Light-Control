package com.yourname.bluetoothlight;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity {

    Button btnConnect, btnLow, btnMedium, btnHigh;
    TextView statusText;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket;
    OutputStream outputStream;

    final int REQUEST_ENABLE_BT = 1;
    final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    String hc05Name = "HC-05";  // You can change if your module has a different name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect = findViewById(R.id.btnConnect);
        btnLow = findViewById(R.id.btnLow);
        btnMedium = findViewById(R.id.btnMedium);
        btnHigh = findViewById(R.id.btnHigh);
        statusText = findViewById(R.id.statusText);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show();
            finish();
        }

        btnConnect.setOnClickListener(view -> connectToHC05());

        btnLow.setOnClickListener(view -> sendCommand("LOW"));
        btnMedium.setOnClickListener(view -> sendCommand("MEDIUM"));
        btnHigh.setOnClickListener(view -> sendCommand("HIGH"));
    }

    void connectToHC05() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent btEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btEnable, REQUEST_ENABLE_BT);
        }

        ProgressDialog progress = ProgressDialog.show(this, "Connecting...", "Please wait");
        new Thread(() -> {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            BluetoothDevice hc05 = null;

            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(hc05Name)) {
                    hc05 = device;
                    break;
                }
            }

            if (hc05 != null) {
                try {
                    bluetoothSocket = hc05.createRfcommSocketToServiceRecord(MY_UUID);
                    bluetoothSocket.connect();
                    outputStream = bluetoothSocket.getOutputStream();

                    runOnUiThread(() -> {
                        progress.dismiss();
                        statusText.setText("Status: Connected");
                        statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    });

                } catch (IOException e) {
                    runOnUiThread(() -> {
                        progress.dismiss();
                        Toast.makeText(this, "Connection Failed", Toast.LENGTH_LONG).show();
                    });
                }
            } else {
                runOnUiThread(() -> {
                    progress.dismiss();
                    Toast.makeText(this, "HC-05 not found. Pair it in Bluetooth settings first.", Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    void sendCommand(String cmd) {
        try {
            if (outputStream != null) {
                outputStream.write((cmd + "\n").getBytes());
                Toast.makeText(this, "Sent: " + cmd, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error sending", Toast.LENGTH_SHORT).show();
        }
    }
}
