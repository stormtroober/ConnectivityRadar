package com.ds.connectivityradar

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ds.connectivityradar.permissions.PermissionManager

class BluetoothHandler(private val activity: MainActivity) {

    private val appContext: Context = activity.applicationContext
    private val permissionManager = PermissionManager(activity)

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getBtPermission() {
        val bluetoothManager: BluetoothManager =
            appContext.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        if (bluetoothAdapter?.isEnabled == false) {
            permissionManager.requestPermission(Manifest.permission.BLUETOOTH_CONNECT)
            requestBluetoothEnable()
        }

        if (bluetoothAdapter?.isEnabled == true) {
            Log.d("com.ds.connectivityradar.BluetoothHandler", "Bluetooth enabled")
        }

        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN
        )

        for (permission in permissions) {
            if (!permissionManager.isPermissionGranted(permission)) {
                permissionManager.requestPermission(permission)
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

    @RequiresApi(Build.VERSION_CODES.S)
    fun discovery(discoveredDevices: MutableList<BluetoothDevice>) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {

                if (ContextCompat.checkSelfPermission(
                        activity.applicationContext, Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        activity, arrayOf(Manifest.permission.BLUETOOTH_SCAN), 1
                    )
                }
                Log.i("com.ds.connectivityradar.BluetoothHandler", "Starting discovery")
                Log.i("Discovery Process", bluetoothAdapter.startDiscovery().toString())

                val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
                val receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        if (device != null) {
                            if (ActivityCompat.checkSelfPermission(
                                    activity, Manifest.permission.BLUETOOTH_CONNECT
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                if(!discoveredDevices.contains(device)) {
                                    discoveredDevices.add(device)
                                }
                                Log.i(
                                    "com.ds.connectivityradar.BluetoothHandler",
                                    "Found device: ${device.name} with address ${device.address}"
                                )
                            } else {
                                // Handle the case where the necessary permissions are not granted
                                Log.i(
                                    "com.ds.connectivityradar.BluetoothHandler",
                                    "Bluetooth connect permission not granted"
                                )
                            }
                        }
                    }
                }
                activity.registerReceiver(receiver, filter)
            }
        }
    }
}