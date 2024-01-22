package com.ds.connectivityradar.permissions

import android.content.pm.PackageManager
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
    }
}