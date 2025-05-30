package com.ds.connectivityradar.bluetooth.communication_threads


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.utils.Constants
import java.io.IOException
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.S)
class ServerThread(private val btAdapter: BluetoothAdapter, private val activity: MainActivity) :
    BluetoothThread(btAdapter, activity) {
    private var serverSocket: BluetoothServerSocket? = null
    private fun initialiseSocket(): BluetoothServerSocket? {
        val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            try {
                btAdapter.listenUsingRfcommWithServiceRecord(
                    "ConnectivityRadar", UUID.fromString(Constants.UUID)
                )
            } catch (e: SecurityException) {
                Log.e("ServerThread", "SecurityException while creating BluetoothServerSocket", e)
                null
            }
        }
        return mmServerSocket
    }

    override fun run() {
        if (channelThread == null) {
            // Make the device discoverable
            val discoverableIntent: Intent =
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                    putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
                }
            activity.startActivity(discoverableIntent)
            serverSocket = initialiseSocket()
            Log.i("ServerThread", "Server bluetooth")
            // Keep listening until exception occurs or a socket is returned.
            var shouldLoop = true
            while (shouldLoop) {
                val socketTmp: BluetoothSocket? = try {
                    serverSocket?.accept()
                } catch (e: IOException) {
                    Log.e("ServerThread", "Socket's accept() method failed", e)
                    shouldLoop = true
                    null
                }
                //serverSocket?.close()
                socketTmp?.also {

                    manageMyConnectedSocket(it, socketTmp)
                    //serverSocket?.close()
                    shouldLoop = true
                }
            }
        }
    }

    private fun manageMyConnectedSocket(socket: BluetoothSocket, socketTmp: BluetoothSocket) {
        channelThread = activity.getHandler()?.let { ChannelThread(socketTmp, it, true) }
        channelThread!!.priority = MAX_PRIORITY
        channelThread?.start()
        socketsConnected.add(Pair(socketTmp, channelThread!!))
        Log.i("ServerThread", "listening to socket to MAC address ${socket.remoteDevice.address}")

    }


}