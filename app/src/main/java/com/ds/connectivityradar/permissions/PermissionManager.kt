package com.ds.connectivityradar.permissions

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ds.connectivityradar.MainActivity

class PermissionManager(private val activity: MainActivity) {

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }

    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            activity.applicationContext, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(permission: String) {
        activity.requestPermissions(arrayOf(permission), REQUEST_ENABLE_BT)
        Log.i(
            "com.ds.connectivityradar.permissions.PermissionManager",
            "Requested permission $permission"
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getBtPermission() {
        val bluetoothManager: BluetoothManager =
            activity.applicationContext.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE
            ),
            REQUEST_ENABLE_BT
        )
        if (bluetoothAdapter?.isEnabled == false) {
            requestPermission(Manifest.permission.BLUETOOTH_CONNECT)
            requestBluetoothEnable()
        }

        if (bluetoothAdapter?.isEnabled == true) {
            Log.d("com.ds.connectivityradar.permissions.PermissionManager", "Bluetooth enabled")
        }

        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADVERTISE
        )

        for (permission in permissions) {
            if (!isPermissionGranted(permission)) {
                requestPermission(permission)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestBluetoothEnable() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_ENABLE_BT
            )
        } else {
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}