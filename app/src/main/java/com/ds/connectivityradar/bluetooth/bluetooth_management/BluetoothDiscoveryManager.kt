package com.ds.connectivityradar.bluetooth.bluetooth_management

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.permissions.PermissionManager

class BluetoothDiscoveryManager(private val activity: MainActivity, private val permissionManager: PermissionManager) {
    private var receiver: BroadcastReceiver? = null

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    fun discovery(discoveredDevices: MutableList<BluetoothDevice>) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter?.isDiscovering == false) {
            bluetoothAdapter.startDiscovery()

            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device != null && !discoveredDevices.contains(device)) {
                        discoveredDevices.add(device)
                    }
                }
            }
            activity.registerReceiver(receiver, filter)
        }
    }

    fun unregisterReceiver() {
        if (receiver != null) {
            activity.unregisterReceiver(receiver)
            receiver = null
        }
    }
}