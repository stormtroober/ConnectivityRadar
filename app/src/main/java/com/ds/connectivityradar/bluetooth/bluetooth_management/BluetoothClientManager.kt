package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.bluetooth.communication_threads.ClientThread

/**
 * This class is responsible for managing the connection to a Bluetooth device.
 * @param activity The activity that uses this class.
 */
class BluetoothClientManager(private val activity: MainActivity, private val bluetoothManager: BluetoothManager){
    private var clientThread: ClientThread? = null

    /**
     * Connects to a Bluetooth device.
     * @param clientDevice The device to connect to.
     * @see ClientThread
     */
    @RequiresApi(Build.VERSION_CODES.S)
    fun connectToClient(clientDevice: BluetoothDevice) {
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {
                clientThread = ClientThread(bluetoothAdapter, clientDevice, activity)
                clientThread!!.start()
            }
        }
    }

    /**
     * Sends a message to the connected socket.
     * @param message The message to be sent.
     */
    fun sendMessageToServer(message: String){
        clientThread?.sendMessage(message)
    }

}