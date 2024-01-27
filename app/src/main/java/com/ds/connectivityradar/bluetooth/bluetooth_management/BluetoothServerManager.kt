package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.util.Log
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.bluetooth.communication_threads.ClientThread
import com.ds.connectivityradar.bluetooth.communication_threads.ServerThread

/**
 * This class is responsible for managing the Bluetooth server.
 * @param serverThread The thread that manages the server.
 * @see ServerThread
 */
class BluetoothServerManager(private val activity: MainActivity, private val bluetoothManager: BluetoothManager) {
    private var isBluetoothServerRunning = false
    private var serverThread: ServerThread? = null

    fun startBluetoothServer() {
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {
                isBluetoothServerRunning = true
                serverThread = ServerThread(bluetoothAdapter, activity)
                serverThread!!.start()
            }
        }
    }

    fun stopBluetoothServer() {
        serverThread?.cancel()
        isBluetoothServerRunning = false
    }

    fun isServerRunning(): Boolean {
        return isBluetoothServerRunning
    }

    fun sendMessageToClient(message: String) {
        serverThread?.sendMessage(message)
    }
}