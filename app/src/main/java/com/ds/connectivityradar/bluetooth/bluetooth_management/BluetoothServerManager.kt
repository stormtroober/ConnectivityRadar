package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.bluetooth.BluetoothAdapter
import com.ds.connectivityradar.bluetooth.communication_threads.ServerThread

/**
 * This class is responsible for managing the Bluetooth server.
 * @param serverThread The thread that manages the server.
 * @see ServerThread
 */
class BluetoothServerManager(private val serverThread: ServerThread) {
    private var isBluetoothServerRunning = false

    fun startBluetoothServer() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {
                isBluetoothServerRunning = true
                serverThread.start()
            }
        }
    }

    fun stopBluetoothServer() {
        serverThread.cancel()
        isBluetoothServerRunning = false
    }

    fun isServerRunning(): Boolean {
        return isBluetoothServerRunning
    }
}