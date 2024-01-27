package com.ds.connectivityradar

import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
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


    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MESSAGE_READ -> {
                    // Handle data read from Bluetooth device
                    val readBytes = msg.obj as ByteArray
                    // Process the received data as needed
                    Log.i("mainActivity received message", String(readBytes))
                    val receivedMessage = String(readBytes)+" Server"
                    btHandler.sendMessage(receivedMessage)
                }
                Constants.MESSAGE_WRITE -> {
                    // Handle data sent to Bluetooth device
                    // ...
                }
                Constants.MESSAGE_TOAST -> {
                    // Handle toast messages (e.g., connection failure)
                    val toastMessage = msg.data.getString("toast")
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

    override fun onDestroy() {
        super.onDestroy()
        btHandler.stopBluetoothServer()
        btHandler.unregisterReceiver()
    }

    public fun getHandler(): Handler {
        return handler
    }

    public fun isSocketRunning() : Boolean{
        return isSocketRunning.value
    }
    public fun getDeviceConnected() : BluetoothDevice?{
        return deviceConnected.value
    }

}
