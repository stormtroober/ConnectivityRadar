package com.ds.connectivityradar.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log
import com.ds.connectivityradar.utils.Constants
import java.io.IOException

class ConnectedThread(private val socket: BluetoothSocket, private val handler: Handler) : Thread() {

    private val inputStream = socket.inputStream
    private val outputStream = socket.outputStream

    override fun run() {
        val buffer = ByteArray(1024)  // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                val bytes = inputStream.read(buffer)
                // Send the obtained bytes to the UI activity.
                val readBytes = ByteArray(bytes)
                System.arraycopy(buffer, 0, readBytes, 0, bytes)

                // Send the obtained bytes to the UI activity.
                handler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, readBytes)
                    .sendToTarget()
            } catch (e: IOException) {
                Log.e("ConnectedThread", "Input stream was disconnected", e)
                break
            }
        }
    }

    // Call this from the main activity to send data to the remote device.
    fun write(bytes: ByteArray) {
        try {
            outputStream.write(bytes)
        } catch (e: IOException) {
            Log.e("ConnectedThread", "Error occurred when sending data", e)
        }
    }

    // Call this method from the main activity to shut down the connection.
    fun cancel() {
        try {
            socket.close()
        } catch (e: IOException) {
            Log.e("ConnectedThread", "Could not close the connect socket", e)
        }
    }
}