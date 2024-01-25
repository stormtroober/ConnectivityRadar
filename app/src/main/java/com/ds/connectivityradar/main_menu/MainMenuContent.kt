package com.ds.connectivityradar.main_menu

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import com.ds.connectivityradar.bluetooth.BluetoothHandler
import com.ds.connectivityradar.ui.theme.ConnectivityRadarTheme

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainContent(
    activity: Activity,
    btHandler: BluetoothHandler,
    discoveredDevices: MutableList<BluetoothDevice>,
    onDeviceClick: (BluetoothDevice) -> Unit
) {
    var btResponse by remember { mutableStateOf("") }
    var btResponseServer by remember { mutableStateOf("") }
    var btResponseClient by remember { mutableStateOf("") }
    var btResponseDiscovery by remember { mutableStateOf("") }
    ConnectivityRadarTheme {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainMenuButton(
                "Discovery", {
                    btResponseDiscovery = "discovery process"
                    btHandler.getBtPermission()
                    btHandler.discovery(discoveredDevices)

                }, "discovery process"
            )
            LazyColumn {
                items(discoveredDevices) { device ->
                    Text(
                        text = "${device.name ?: "Unknown Device"} - ${device.address}",
                        modifier = Modifier.clickable {
                            onDeviceClick(device)
                        }
                    )
                }
            }

            MainMenuButton(
                "Crea Server...",
                {
                    try{
                        //Se il server sta già runnando, lo stoppo e ne creo uno nuovo
                        if(!btHandler.isServerRunning()){
                            btHandler.getBtPermission()
                            btHandler.startBluetoothServer()
                            btResponseServer = "Server online"
                        }
                        else{
                            btResponseServer = "Server Online already"
                        }

                    } catch (e: Exception) {
                        btResponseServer = "Server offline - errors"
                    }
                },
                btResponseServer
            )



        }
    }
}