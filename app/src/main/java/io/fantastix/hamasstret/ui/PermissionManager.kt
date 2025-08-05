package io.fantastix.hamasstret.ui

import android.Manifest
import android.content.Context
import androidx.activity.result.ActivityResultLauncher

class PermissionManager(
    private val context: Context,
    private val locationPermissionRequest: ActivityResultLauncher<Array<String>>
) {
    fun requestPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}