package com.ds.connectivityradar

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


class BluetoothHandler(private val activity: MainActivity) {

    private var appContext = MainActivity.appContext


    @RequiresApi(Build.VERSION_CODES.S)
    fun connect(){
        try {
            val bluetoothManager: BluetoothManager = appContext.getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        appContext,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    activity.requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
                    return
                }
                activity.startActivityForResult(enableBtIntent, 1)
            }
        } catch (e: Exception) {
            // Log the exception
             Log.e("BluetoothHandler", "Error connecting to Bluetooth", e)
        }
    }
}