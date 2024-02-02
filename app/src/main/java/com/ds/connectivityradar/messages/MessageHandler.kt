package com.ds.connectivityradar.messages

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.bluetooth.BluetoothHandler
import com.ds.connectivityradar.utils.Constants
import com.ds.connectivityradar.utils.Constants.KEEP_ALIVE_MESSAGE
import java.time.Clock

class MessageHandler(private val activity: MainActivity, private val btHandler: BluetoothHandler) : Handler(Looper.getMainLooper()) {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            Constants.MESSAGE_RECEIVED_CLIENT -> {
                val receivedBytes = msg.obj as ByteArray // assuming msg.obj is your ByteArray
                val receivedString = String(receivedBytes)
                if (ignoreKeepAlive(receivedString)) {
                    return
                }
                if(receivedString == "ACKNOWLEDGE"){
                    val difference = Clock.systemUTC().millis() - activity.timeOfSendingClient!!
                    Log.i("Client", "Round Trip Time: $difference")
                    activity.runOnUiThread {
                        Toast.makeText(
                            activity,
                            "Time difference: $difference ms", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            Constants.MESSAGE_RECEIVED_SERVER-> {
                val receivedBytes = msg.obj as ByteArray // assuming msg.obj is your ByteArray
                val receivedString = String(receivedBytes)
                if (ignoreKeepAlive(receivedString)) {
                    return
                }
                btHandler.sendMessage("ACKNOWLEDGE")
            }
        }
    }

    private fun ignoreKeepAlive(receivedString: String): Boolean {
        return receivedString == KEEP_ALIVE_MESSAGE
    }
}