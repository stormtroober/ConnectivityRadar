package com.ds.connectivityradar.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.util.Log

class BluetoothSocketHandler(private val socket: BluetoothSocket, private val handler: Handler) : Thread(){

    private val socketInput = socket.inputStream
    private val socketOutput = socket.outputStream
    private val socketBuffer = ByteArray(1024)
}