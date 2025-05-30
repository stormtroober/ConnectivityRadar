package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.bluetooth.communication_threads.ServerThread

/**
 * This class is responsible for managing the Bluetooth server.
 * @param serverThread The thread that manages the server.
 * @see ServerThread
 */
class BluetoothServerManager(private val activity: MainActivity, private val bluetoothManager: BluetoothManager) : BluetoothConnectionManager(
    bluetoothManager
){
    private var isBluetoothServerRunning = false

    @RequiresApi(Build.VERSION_CODES.S)
    fun startBluetoothServer() {
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {
                isBluetoothServerRunning = true
                btThread = ServerThread(bluetoothAdapter, activity)
                btThread!!.start()
            }
        }
    }
    //TODO
//    @RequiresApi(Build.VERSION_CODES.S)
//    fun stopBluetoothServer() {
//        btThread?.cancel()
//        isBluetoothServerRunning = false
//    }

    fun isServerRunning(): Boolean {
        return isBluetoothServerRunning
    }


}