package com.ds.connectivityradar.bluetooth.communication_threads

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.permissions.PermissionManager

@RequiresApi(Build.VERSION_CODES.S)
open class BluetoothThread (btAdapter: BluetoothAdapter, activity: MainActivity) : Thread() {
    private val permissionManager = PermissionManager(activity)
    val bluetoothAdapter: BluetoothAdapter = btAdapter
    open val socketsConnected: MutableList<Pair<BluetoothSocket, ChannelThread>> = mutableListOf()
    open var channelThread: ChannelThread? = null

    init {
        if(permissionManager.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT)){
            permissionManager.requestPermission(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            Log.e("ServerThread", "Permission not granted")
        }
    }
    //TODO
//    fun cancel() {
//        try {
//            socket?.close()
//        } catch (e: IOException) {
//            Log.e("BluetoothThread", "Could not close the connect socket", e)
//        }
//    }

    fun sendMessage(message: String) {
        channelThread?.write(message.toByteArray())
    }

    fun sendBroadcastMessage(message: String) {
        socketsConnected.forEach() { socket ->
            socket.second.write(message.toByteArray())
        }
    }

}


