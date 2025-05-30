package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.bluetooth.BluetoothHandler

class KeepAliveThread(private val btHandler: BluetoothHandler): Thread() {
    private var isRunning = false

    @RequiresApi(Build.VERSION_CODES.S)
    override fun run() {
        sleep(2000)
        Log.i("KeepAliveThread", "KeepAliveThread started")
        while (isRunning) {
            //btHandler.sendMessage("KeepAlive")
            sleep(2000)
        }
    }

    fun startThread() {
        isRunning = true
        start()
    }
    fun stopThread() {
        isRunning = false
    }

    fun isRunning(): Boolean {
        return isRunning
    }

}