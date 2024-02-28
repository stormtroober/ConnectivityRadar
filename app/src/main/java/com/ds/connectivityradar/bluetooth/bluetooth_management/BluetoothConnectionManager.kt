package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.bluetooth.BluetoothClass.Device
import android.bluetooth.BluetoothManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.bluetooth.communication_threads.BluetoothThread

open class BluetoothConnectionManager(private val bluetoothManager: BluetoothManager) {

    //Here is needed a list of devices
    open var btThread: BluetoothThread? = null
    //open val connectedDevices: MutableList<Device> = mutableListOf()

    //Here you should have functions like sendMessageToServer
    @RequiresApi(Build.VERSION_CODES.S)
    fun sendMessage(message: String) {
        btThread?.sendMessage(message)
    }
}