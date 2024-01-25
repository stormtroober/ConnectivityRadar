package com.ds.connectivityradar

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ds.connectivityradar.bluetooth.BluetoothHandler
import com.ds.connectivityradar.main_menu.MainContent
import com.ds.connectivityradar.main_menu.MainScreen


class MainActivity : ComponentActivity() {

    private lateinit var btHandler: BluetoothHandler

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
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MainContent(
        MainActivity(),
        BluetoothHandler(MainActivity()),
        mutableListOf(),
        onDeviceClick = { /* Handle click event here */ }
    )
}
