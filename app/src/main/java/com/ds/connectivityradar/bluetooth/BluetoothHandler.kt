package com.ds.connectivityradar.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.bluetooth.bluetooth_management.BluetoothClientManager
import com.ds.connectivityradar.bluetooth.bluetooth_management.BluetoothConnectionManager
import com.ds.connectivityradar.bluetooth.bluetooth_management.BluetoothDiscoveryManager
import com.ds.connectivityradar.bluetooth.bluetooth_management.BluetoothServerManager
import com.ds.connectivityradar.bluetooth.bluetooth_management.KeepAliveThread
import com.ds.connectivityradar.permissions.PermissionManager

class BluetoothHandler(private val activity: MainActivity) {
    private val appContext: Context = activity.applicationContext
    private val permissionManager = PermissionManager(activity)
    private val bluetoothManager: BluetoothManager =
        appContext.getSystemService(BluetoothManager::class.java)

    private val bluetoothDiscoveryManager = BluetoothDiscoveryManager(activity, permissionManager)
    private var bluetoothConnectionManager: BluetoothConnectionManager? = null
    private var keepAliveThread: KeepAliveThread = KeepAliveThread(this)

    @RequiresApi(Build.VERSION_CODES.S)
    fun startBluetoothServer() {
        bluetoothConnectionManager = BluetoothServerManager(activity, bluetoothManager)
        (bluetoothConnectionManager as? BluetoothServerManager)?.startBluetoothServer()
    }

    fun startKeepAliveThread() {
        keepAliveThread.startThread()
    }

    fun stopKeepAliveThread() {
        keepAliveThread.stopThread()
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
        bluetoothConnectionManager = BluetoothClientManager(activity, bluetoothManager)
        (bluetoothConnectionManager as? BluetoothClientManager)?.connectToClient(device)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun sendMessage(message : String) {
        bluetoothConnectionManager?.sendMessage(message)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun stopBluetoothServer() {
        (bluetoothConnectionManager as? BluetoothServerManager)?.stopBluetoothServer()
    }

    fun isServerRunning(): Boolean {
        return (bluetoothConnectionManager as? BluetoothServerManager)?.isServerRunning() ?: false
    }

    fun unregisterReceiver() {
        bluetoothDiscoveryManager.unregisterReceiver()
    }

    fun isKeepAliveThreadRunning(): Boolean {
        return keepAliveThread.isRunning()
    }
}