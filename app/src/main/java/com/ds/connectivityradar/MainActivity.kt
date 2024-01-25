package com.ds.connectivityradar

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ds.connectivityradar.bluetooth.BluetoothHandler
import com.ds.connectivityradar.bluetooth.BluetoothService
import com.ds.connectivityradar.main_menu.MainContent
import com.ds.connectivityradar.main_menu.MainScreen
import com.ds.connectivityradar.utils.Constants


class MainActivity : ComponentActivity() {

    private lateinit var btHandler: BluetoothHandler
    private lateinit var bluetoothService: BluetoothService

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MESSAGE_READ -> {
                    // Handle data read from Bluetooth device
                    val readBytes = msg.obj as ByteArray
                    // Process the received data as needed
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

    public fun getBluetoothService(): BluetoothService {
        return bluetoothService
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bluetoothService = BluetoothService(handler)
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
}
