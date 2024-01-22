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

class BluetoothHandler(private val activity: MainActivity) {

    private val appContext: Context = activity.applicationContext

    @RequiresApi(Build.VERSION_CODES.S)
    fun getBtPermission() {
        try {
            val bluetoothManager: BluetoothManager =
                appContext.getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        appContext, Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    activity.requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
                    Log.d("com.ds.connectivityradar.BluetoothHandler", "Bluetooth permission granted. Returning")
                    return
                }
                activity.startActivityForResult(enableBtIntent, 1)
            }
            if (bluetoothAdapter != null) {
                if (bluetoothAdapter.isEnabled) {
                    Log.d("com.ds.connectivityradar.BluetoothHandler", "Bluetooth enabled")
                }
            }
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN
            )

            if (ContextCompat.checkSelfPermission(
                    activity.applicationContext, permissions[0]
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity.applicationContext, permissions[1]
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity.applicationContext, permissions[2]
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity.applicationContext, permissions[3]
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    activity.applicationContext, permissions[4]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity, permissions, 1)
            }

        } catch (e: Exception) {
            // Log the exception
            Log.e("com.ds.connectivityradar.BluetoothHandler", "Error connecting to Bluetooth", e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun discovery(discoveredDevices: MutableList<BluetoothDevice>) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {

                if (ContextCompat.checkSelfPermission(
                        activity.applicationContext,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.BLUETOOTH_SCAN), 1
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
                                    activity,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                discoveredDevices.add(device)
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