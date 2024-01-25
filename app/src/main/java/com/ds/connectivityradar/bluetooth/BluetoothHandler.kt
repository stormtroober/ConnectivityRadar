package com.ds.connectivityradar.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
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
import com.ds.connectivityradar.MainActivity
import com.ds.connectivityradar.permissions.PermissionManager
import java.io.IOException
import java.util.UUID

class BluetoothHandler(private val activity: MainActivity) {

    private val appContext: Context = activity.applicationContext
    private val permissionManager = PermissionManager(activity)
    private val bluetoothManager: BluetoothManager =
        appContext.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private val serverThread : ServerThread = ServerThread(bluetoothManager.adapter, activity)
    private var isBluetoothServerRunning = false

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }

    fun startBluetoothServer() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {
                isBluetoothServerRunning = true
                serverThread.start()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    fun getBtPermission() {
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_ADVERTISE), REQUEST_ENABLE_BT
        )
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
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADVERTISE
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
                    Log.i("com.ds.connectivityradar.BluetoothHandler", "BLUETOOTH_SCAN permission not granted")
                    ActivityCompat.requestPermissions(
                        activity, arrayOf(Manifest.permission.BLUETOOTH_SCAN), 1
                    )
                } else {
                    Log.i("com.ds.connectivityradar.BluetoothHandler", "BLUETOOTH_SCAN permission granted")
                }

                if (ContextCompat.checkSelfPermission(
                        activity.applicationContext, Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.i("com.ds.connectivityradar.BluetoothHandler", "BLUETOOTH_CONNECT permission not granted")
                    ActivityCompat.requestPermissions(
                        activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1
                    )
                } else {
                    Log.i("com.ds.connectivityradar.BluetoothHandler", "BLUETOOTH_CONNECT permission granted")
                }

                if (bluetoothAdapter.isDiscovering) {
                    Log.i("com.ds.connectivityradar.BluetoothHandler", "Discovery process already in progress")
                } else {
                    Log.i("com.ds.connectivityradar.BluetoothHandler", "Starting discovery")
                    val isDiscoveryStarted = bluetoothAdapter.startDiscovery()
                    Log.i("com.ds.connectivityradar.BluetoothHandler", "Discovery started: $isDiscoveryStarted")
                }

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

    @RequiresApi(Build.VERSION_CODES.S)
    fun connectToDevice(device: BluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                1
            )
            return
        }
        Log.i("com.ds.connectivityradar.BluetoothHandler", "Connecting to device ${device.name} with address ${device.address}")
        startBluetoothClient(device)
        
    }

    fun stopBluetoothServer() {
        serverThread.cancel()
        isBluetoothServerRunning = false
    }

    private fun startBluetoothClient(device: BluetoothDevice?) {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {
                if (device != null) {
                    val clientThread = ClientThread(bluetoothAdapter, device, activity)
                    clientThread.start()
                }
            }
        }
    }

    fun isServerRunning(): Boolean {
        return isBluetoothServerRunning
    }
}