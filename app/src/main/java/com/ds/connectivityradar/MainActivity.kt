package com.ds.connectivityradar

import BluetoothHandler
import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.ds.connectivityradar.ui.theme.ConnectivityRadarTheme


class MainActivity : ComponentActivity() {
    private val btResponse = mutableStateOf("")

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppContent(BluetoothHandler(this))
        }
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action.toString()
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                    //val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    print(deviceHardwareAddress)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppContent(btHandler: BluetoothHandler) {
    var btResponse by remember { mutableStateOf("") }
    ConnectivityRadarTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                onClick = {
                    //btResponse è stato scritto per salvare la risposta del discovery del bt
                    btResponse = "Risposta bluetooth..."
                    btHandler.getBtPermission()
                }) {
                Text("Connetti al bluetooth")
            }
            Text(
                modifier = Modifier.padding(5.dp),
                text = btResponse
            )

            Button(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                onClick = {
                    //btResponse è stato scritto per salvare la risposta del discovery del bt
                    btResponse = "discovery process"
                    btHandler.discovery()
                }) {
                Text("Discovery")
            }
            Text(
                modifier = Modifier.padding(5.dp),
                text = "discovery process"
            )

        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppContent(BluetoothHandler(MainActivity()))
}
