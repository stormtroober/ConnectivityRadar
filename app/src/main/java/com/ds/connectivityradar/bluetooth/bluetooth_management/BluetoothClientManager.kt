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
class BluetoothClientManager(private val activity: MainActivity, private val bluetoothManager: BluetoothManager) : BluetoothConnectionManager(
    bluetoothManager
){


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
                btThread = ClientThread(bluetoothAdapter, clientDevice, activity)
                btThread!!.start()
            }
        }
    }



}