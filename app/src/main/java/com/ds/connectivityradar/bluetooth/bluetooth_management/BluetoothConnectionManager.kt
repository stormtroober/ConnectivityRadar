package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.bluetooth.BluetoothManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.bluetooth.communication_threads.BluetoothThread
import com.ds.connectivityradar.bluetooth.communication_threads.ServerThread

open class BluetoothConnectionManager (private val activity: MainActivity, private val bluetoothManager: BluetoothManager) {
    open var btThread: BluetoothThread? = null

    @RequiresApi(Build.VERSION_CODES.S)
    fun sendMessage(message: String) {
        btThread?.sendMessage(message)
    }
}