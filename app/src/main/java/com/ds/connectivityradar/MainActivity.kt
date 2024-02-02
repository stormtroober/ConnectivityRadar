package com.ds.connectivityradar

import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import com.ds.connectivityradar.bluetooth.BluetoothHandler
import com.ds.connectivityradar.main_menu.MainScreen
import com.ds.connectivityradar.messages.MessageHandler
import com.ds.connectivityradar.utils.Constants
import java.time.Clock


class MainActivity : ComponentActivity() {

    private lateinit var btHandler: BluetoothHandler
    var isSocketRunning = mutableStateOf(false)
    var deviceConnected = mutableStateOf<BluetoothDevice?>(null)
    public var timeOfSendingClient : Long? = null

    private var handler: Handler? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btHandler = BluetoothHandler(this)
        handler = MessageHandler(this, btHandler)
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

    fun getHandler(): Handler? {
        return handler
    }

    fun isSocketRunning(): Boolean {
        return isSocketRunning.value
    }

    fun getDeviceConnected(): BluetoothDevice? {
        return deviceConnected.value
    }

}
