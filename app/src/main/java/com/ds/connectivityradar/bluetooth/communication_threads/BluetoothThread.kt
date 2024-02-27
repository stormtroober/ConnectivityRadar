package com.ds.connectivityradar.bluetooth.communication_threads

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.permissions.PermissionManager
import java.io.IOException

@RequiresApi(Build.VERSION_CODES.S)
open class BluetoothThread (btAdapter: BluetoothAdapter, activity: MainActivity) : Thread() {
    private val permissionManager = PermissionManager(activity)
    val bluetoothAdapter: BluetoothAdapter = btAdapter
    open var socket: BluetoothSocket? = null
    open var channelThread: ChannelThread? = null

    init {
        if(permissionManager.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT)){
            permissionManager.requestPermission(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            Log.e("ServerThread", "Permission not granted")
        }
    }
    fun cancel() {
        try {
            socket?.close()
        } catch (e: IOException) {
            Log.e("BluetoothThread", "Could not close the connect socket", e)
        }
    }

    fun sendMessage(message: String) {
        //Log.i("BluetoothThread", "Sent Message to the socket")
        channelThread?.write(message.toByteArray())
    }

}


