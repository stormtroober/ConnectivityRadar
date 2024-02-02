package com.ds.connectivityradar.main_menu

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.util.Log
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
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.bluetooth.BluetoothHandler
import com.ds.connectivityradar.ui.theme.ConnectivityRadarTheme
import java.time.Clock

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainContent(
    activity: Activity,
    btHandler: BluetoothHandler,
    discoveredDevices: MutableList<BluetoothDevice>,
    onDeviceClick: (BluetoothDevice) -> Unit
) {
    var timeOfSending: Long? = null
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
                    btResponseServer = try{
                        //Se il server sta già runnando, lo stoppo e ne creo uno nuovo
                        if(!btHandler.isServerRunning()){
                            btHandler.getBtPermission()
                            btHandler.startBluetoothServer()
                            "Server online"
                        } else{
                            "Server Online already"
                        }

                    } catch (e: Exception) {
                        "Server offline - errors"
                    }
                },
                btResponseServer
            )
            
            if((activity as MainActivity).isSocketRunning()){
                val device = activity.getDeviceConnected()
                val deviceName = (device?.name ?: "Unknown Device")
                MainMenuButton(buttonText = "Send Message to " + deviceName,
                    buttonAction = {
                        if (device != null) {
                            val time = Clock.systemUTC().millis()
                            btHandler.sendMessage(time.toString())
                            Log.i("Client", "Message sent to ${device.name}")
                        }

                    },
                    text = "")
            }



        }
    }
}