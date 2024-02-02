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
import com.ds.connectivityradar.utils.Constants
import java.time.Clock


class MainActivity : ComponentActivity() {

    private lateinit var btHandler: BluetoothHandler
    var isSocketRunning = mutableStateOf(false)
    var deviceConnected = mutableStateOf<BluetoothDevice?>(null)
    public var timeOfSendingClient : Long? = null

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MESSAGE_RECEIVED_CLIENT -> {
                    val receivedBytes = msg.obj as ByteArray // assuming msg.obj is your ByteArray

                    if(String(receivedBytes) == "ACKNOWLEDGE"){
                        val difference = Clock.systemUTC().millis() - timeOfSendingClient!!
                        Log.i("Client", "Round Trip Time: $difference")
                        runOnUiThread(Runnable {
                            Toast.makeText(this@MainActivity,
                                "Time difference: $difference ms", Toast.LENGTH_SHORT).show()
                        })
                    }
                }

                Constants.MESSAGE_RECEIVED_SERVER-> {
                    Log.i("Server", "Received message: ${msg.obj}")
                    btHandler.sendMessage("ACKNOWLEDGE")
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
