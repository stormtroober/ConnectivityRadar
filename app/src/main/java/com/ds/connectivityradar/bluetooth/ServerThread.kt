package com.ds.connectivityradar.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.util.UUID
import android.util.Log
import androidx.core.content.ContextCompat


import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.permissions.PermissionManager
import java.io.IOException

class ServerThread(private val btAdapter: BluetoothAdapter, private val activity: MainActivity) : Thread() {

    private val permissionManager = PermissionManager(activity)
    private var serverSocket: BluetoothServerSocket? = null

    private fun initialiseSocket(): BluetoothServerSocket? {
        val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            try {
                if(permissionManager.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT)) {
                    btAdapter.listenUsingRfcommWithServiceRecord("ConnectivityRadar", UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                } else {
                    null
                }
            } catch (e: SecurityException) {
                Log.e("ServerThread", "SecurityException while creating BluetoothServerSocket", e)
                null
            }
        }
        val socket = mmServerSocket
        return socket
    }

    override fun run() {
        // Make the device discoverable
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        }
        activity.startActivity(discoverableIntent)
        serverSocket = initialiseSocket()
        Log.i("ServerThread", "Server bluetooth")
        // Keep listening until exception occurs or a socket is returned.
        var shouldLoop = true
        while (shouldLoop) {
            val socket: BluetoothSocket? = try {
                serverSocket?.accept()
            } catch (e: IOException) {
                Log.e("ServerThread", "Socket's accept() method failed", e)
                shouldLoop = false
                null
            }
            socket?.also {
                manageMyConnectedSocket(it)
                //serverSocket?.close()
                shouldLoop = false
            }
        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        val connectedThread = ConnectedThread(socket, activity.getHandler())
        connectedThread.start()
        Log.i("ServerThread", "listening to socket.")
    }

    // Closes the connect socket and causes the thread to finish.
    fun cancel() {
        try {
            serverSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the connect socket", e)
        }
    }
}