package com.ds.connectivityradar.main_menu

import com.ds.connectivityradar.BluetoothHandler
import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ds.connectivityradar.ui.theme.ConnectivityRadarTheme

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainContent(
    activity: Activity,
    btHandler: BluetoothHandler,
    discoveredDevices: MutableList<BluetoothDevice>,
) {
    var btResponse by remember { mutableStateOf("") }
    ConnectivityRadarTheme {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainMenuButton(
                "Connetti al bluetooth",
                { btResponse = "Risposta bluetooth..."; btHandler.getBtPermission() },
                btResponse
            )
            MainMenuButton(
                "Discovery", {
                    btResponse = "discovery process"
                    if (ContextCompat.checkSelfPermission(
                            activity, Manifest.permission.BLUETOOTH_CONNECT
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        btHandler.discovery(discoveredDevices)
                    } else {
                        // Request the necessary permissions
                        ActivityCompat.requestPermissions(
                            activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1
                        )
                    }
                }, "discovery process"
            )
            LazyColumn {
                items(discoveredDevices) { device ->
                    Text(text = "${device.name ?: "Unknown Device"} - ${device.address}")
                }
            }

            MainMenuButton(
                "Crea Server...",
                { btResponse = "Risposta bluetooth..."; },
                btResponse
            )

            MainMenuButton(
                "Unisciti come Client...",
                { btResponse = "Risposta bluetooth..."; },
                btResponse
            )

        }
    }
}