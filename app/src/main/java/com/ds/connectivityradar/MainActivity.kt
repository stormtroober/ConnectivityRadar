package com.ds.connectivityradar

import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.ds.connectivityradar.bluetooth.BluetoothHandler
import com.ds.connectivityradar.main_menu.MainScreen
import com.ds.connectivityradar.utils.Constants


class MainActivity : ComponentActivity() {

    private lateinit var btHandler: BluetoothHandler
    var isSocketRunning = mutableStateOf(false)
    var deviceConnected = mutableStateOf<BluetoothDevice?>(null)
    var timeReceived: Long? = null

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MESSAGE_READ -> {
                    val receivedBytes = msg.obj as ByteArray // assuming msg.obj is your ByteArray
                    val receivedTimeStr = String(receivedBytes)
                    val receivedTimeMillis = receivedTimeStr.toLong()
                    var timeDifference: Long = 0

                    val currentTimeMillis = System.currentTimeMillis()
                    if(timeReceived == null) {
                        timeReceived = currentTimeMillis
                    }
                    else{
                        timeDifference = receivedTimeMillis - timeReceived!!
                    }
                    Log.i("TimeDifference", timeDifference.toString())
                }

                Constants.MESSAGE_WRITE -> {
                    // Handle data sent to Bluetooth device
                    // ...
                }

                Constants.MESSAGE_TOAST -> {
                    // Handle toast messages (e.g., connection failure)
                    msg.data.getString("toast")
                    // Show toast message to the user
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btHandler = BluetoothHandler(this)
        setContent {
            MainScreen(btHandler)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDestroy() {
        super.onDestroy()
        btHandler.stopBluetoothServer()
        btHandler.unregisterReceiver()
    }

    fun getHandler(): Handler {
        return handler
    }

    fun isSocketRunning(): Boolean {
        return isSocketRunning.value
    }

    fun getDeviceConnected(): BluetoothDevice? {
        return deviceConnected.value
    }

}
