package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.bluetooth.BluetoothClass.Device
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.bluetooth.communication_threads.BluetoothThread

open class BluetoothConnectionManager(private val bluetoothManager: BluetoothManager) {

    //Here is needed a list of devices
    open var btThread: BluetoothThread? = null

    @RequiresApi(Build.VERSION_CODES.S)
    fun sendMessage(message: String) {
        btThread?.sendMessage(message)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun broadcastMessage(message: String) {
        btThread?.sendBroadcastMessage(message)
    }
}