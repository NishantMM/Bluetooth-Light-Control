package com.example.btcontrol

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.btcontrol.ui.theme.BTControlTheme
import java.io.IOException
import java.util.*

class MainActivity : ComponentActivity() {
    private val deviceAddress = "00:25:02:00:68:41"  // Replace with your HC-05 MAC
    private var bluetoothSocket: BluetoothSocket? = null
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request Bluetooth permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
            1
        )

        setContent {
            BTControlTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BluetoothControlUI()
                }
            }
        }
    }

    @Composable
    fun BluetoothControlUI() {
        val context = LocalContext.current
        var isConnected by remember { mutableStateOf(false) }
        var statusText by remember { mutableStateOf("Status: Not Connected") }

        fun connectToBluetooth() {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                Toast.makeText(context, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
                return
            }

            val device: BluetoothDevice? = bluetoothAdapter.bondedDevices.find {
                it.address == deviceAddress
            }

            if (device == null) {
                Toast.makeText(context, "Device not found", Toast.LENGTH_SHORT).show()
                return
            }

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
                bluetoothSocket?.connect()
                isConnected = true
                statusText = "Status: Connected"
                Toast.makeText(context, "Connected to device", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                statusText = "Status: Failed to connect"
                Toast.makeText(context, "Connection failed: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        fun sendCommand(command: String) {
            try {
                bluetoothSocket?.outputStream?.write(command.toByteArray())
                Toast.makeText(context, "Sent: $command", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(context, "Failed to send: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = statusText, style = MaterialTheme.typography.titleMedium)

            Button(onClick = { connectToBluetooth() }) {
                Text("CONNECT")
            }

            if (isConnected) {
                Button(onClick = { sendCommand("L") }) {
                    Text("LOW")
                }
                Button(onClick = { sendCommand("M") }) {
                    Text("MEDIUM")
                }
                Button(onClick = { sendCommand("H") }) {
                    Text("HIGH")
                }
                Button(
                    onClick = { sendCommand("O") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("OFF", color = Color.White)
                }
            }
        }
    }
}
