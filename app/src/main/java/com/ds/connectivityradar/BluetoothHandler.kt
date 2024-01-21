import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ds.connectivityradar.MainActivity

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
                        appContext,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    activity.requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
                    Log.d("BluetoothHandler", "Bluetooth permission granted. Returning")
                    return
                }
                activity.startActivityForResult(enableBtIntent, 1)
            }
            if (bluetoothAdapter != null) {
                if (bluetoothAdapter.isEnabled) {
                    Log.d("BluetoothHandler", "Bluetooth enabled")
                }
            }
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN
            )

            if (ContextCompat.checkSelfPermission(activity.applicationContext, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.applicationContext, permissions[1]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.applicationContext, permissions[2]) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity.applicationContext, permissions[3]) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(activity, permissions, 1)
            }

        } catch (e: Exception) {
            // Log the exception
            Log.e("BluetoothHandler", "Error connecting to Bluetooth", e)
        }
    }

    fun discovery() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled) {
                Log.d("BluetoothHandler", "Bluetooth enabled")
                if (ActivityCompat.checkSelfPermission(
                        activity.applicationContext,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                bluetoothAdapter.startDiscovery()
            }
        }
    }
}