package com.ds.connectivityradar.bluetooth.communication_threads

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.utils.Constants
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("MissingPermission")
class ClientThread(
    adapter: BluetoothAdapter,
    private val device: BluetoothDevice,
    private val activity: MainActivity,
) : BluetoothThread(adapter, activity) {

    init {
        socket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.UUID))
    }

    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.

        bluetoothAdapter.cancelDiscovery()
        if (connectedThread == null) {
            socket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()

                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket)
            }

        }

    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket) {
        Log.i("ClientThread", "manageSocket")
        //val connectedThread = ConnectedThread(socket, activity.getHandler())
        connectedThread = ConnectedThread(socket, activity.getHandler(), false)
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