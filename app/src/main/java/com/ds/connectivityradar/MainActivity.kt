package com.ds.connectivityradar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ds.connectivityradar.main_menu.MainContent
import com.ds.connectivityradar.main_menu.MainScreen


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(BluetoothHandler(this))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MainContent(
        MainActivity(), BluetoothHandler(MainActivity()), mutableListOf()
    )
}
