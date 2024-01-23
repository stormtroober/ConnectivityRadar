package com.ds.connectivityradar.main_menu

import com.ds.connectivityradar.BluetoothHandler
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainScreen(btHandler: BluetoothHandler) {
    val discoveredDevices = remember { mutableStateListOf<BluetoothDevice>() }
    MainContent(
        LocalContext.current as Activity,
        btHandler,
        discoveredDevices
    ) { device ->
        btHandler.connectToDevice(device)
    }
}