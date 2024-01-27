package com.ds.connectivityradar.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.bluetooth.bluetooth_management.BluetoothClientManager
import com.ds.connectivityradar.bluetooth.bluetooth_management.BluetoothDiscoveryManager
import com.ds.connectivityradar.bluetooth.bluetooth_management.BluetoothServerManager
import com.ds.connectivityradar.permissions.PermissionManager

class BluetoothHandler(activity: MainActivity) {

    private val appContext: Context = activity.applicationContext
    private val permissionManager = PermissionManager(activity)
    private val bluetoothManager: BluetoothManager =
        appContext.getSystemService(BluetoothManager::class.java)

    private val bluetoothDiscoveryManager = BluetoothDiscoveryManager(activity, permissionManager)
    private val bluetoothClientManager = BluetoothClientManager(activity, bluetoothManager)
    private val bluetoothServerManager = BluetoothServerManager(activity, bluetoothManager)

    fun startBluetoothServer() {
        bluetoothServerManager.startBluetoothServer()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getBtPermission() {
        permissionManager.getBtPermission()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun discovery(discoveredDevices: MutableList<BluetoothDevice>) {
        bluetoothDiscoveryManager.discovery(discoveredDevices)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun connectToDevice(device: BluetoothDevice) {
        bluetoothClientManager.connectToClient(device)
    }

    fun sendMessageToServer(message: String) {
        bluetoothClientManager.sendMessageToServer(message)
    }

    fun sendMessageToClient(message: String) {
        bluetoothServerManager.sendMessageToClient(message)
    }

    fun stopBluetoothServer() {
        bluetoothServerManager.stopBluetoothServer()
    }

    fun isServerRunning(): Boolean {
        return bluetoothServerManager.isServerRunning()
    }

    fun unregisterReceiver() {
        bluetoothDiscoveryManager.unregisterReceiver()
    }
}