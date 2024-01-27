package com.ds.connectivityradar.bluetooth.communication_threads

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import android.widget.Toast
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.permissions.PermissionManager
import com.ds.connectivityradar.utils.Constants
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class ClientThread (adapter: BluetoothAdapter, private val device: BluetoothDevice, private val activity: MainActivity) : Thread() {
    private val permissionManager = PermissionManager(activity)
    private val bluetoothAdapter: BluetoothAdapter = adapter
    private var clientSocket: BluetoothSocket? = null
    private var connectedThread: ConnectedThread? = null
    init {
        if(permissionManager.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT)){
            clientSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.UUID))
        } else {
            null
            Log.e("ClientThread", "Permission not granted")
        }
    }


    public override fun run() {
        // Cancel discovery because it otherwise slows down the connection.

        bluetoothAdapter.cancelDiscovery()

        clientSocket?.let { socket ->
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            socket.connect()

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageMyConnectedSocket(socket)
        }
    }

    fun sendMessage(message: String) {
        Log.i("ClientThread", "Sent Message to the socket")

        connectedThread?.write(message.toByteArray())
    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            clientSocket?.close()
        } catch (e: IOException) {
            Log.e("ClientThread", "Could not close the client socket", e)
        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        Log.i("ClientThread", "manageSocket")
        //val connectedThread = ConnectedThread(socket, activity.getHandler())
        connectedThread = ConnectedThread(socket, activity.getHandler())
        connectedThread?.start()
        //connectedThread.start()
        Log.i("ClientThread", "Socket is up and running")
        activity.runOnUiThread {
            Toast.makeText(activity, "Socket is up and running", Toast.LENGTH_SHORT).show()
            activity.deviceConnected.value = device
            activity.isSocketRunning.value = true
        }
    }
}