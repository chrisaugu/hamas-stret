package io.fantastix.hamasstret.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import io.fantastix.hamasstret.network.LocationService
import io.fantastix.hamasstret.repository.LocationRepository
import io.fantastix.hamasstret.utils.PermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocationViewModel(
    private val context: Context
) : ViewModel() {
    private val _permissionState = MutableStateFlow<PermissionState>(PermissionState.UNKNOWN)
    val permissionState: StateFlow<PermissionState> = _permissionState

    private val _locationState = MutableStateFlow<LocationState>(LocationState.IDLE)
    val locationState: StateFlow<LocationState> = _locationState

    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val latitude = it.getDoubleExtra("latitude", Double.MIN_VALUE)
                val longitude = it.getDoubleExtra("longitude", Double.MIN_VALUE)
                val accuracy = it.getFloatExtra("accuracy", 0f)
                val time = it.getLongExtra("time", 0L)

                if (latitude != Double.MIN_VALUE && longitude != Double.MIN_VALUE) {
                    val location = Location(LocationManager.GPS_PROVIDER).apply {
                        this.latitude = latitude
                        this.longitude = longitude
                        this.accuracy = accuracy
                        this.time = time
                    }

//                    LocationRepository.updateLocation(location)
                }
            }
        }
    }

    init {
        checkPermissions()
        registerReceiver()
    }

    private fun registerReceiver() {
        val filter = IntentFilter("LOCATION_UPDATE")
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(locationReceiver, filter)
    }

    fun checkPermissions() {
        _permissionState.value = when {
            PermissionUtils.hasLocationPermission(context) -> {
                if (PermissionUtils.hasBackgroundLocationPermission(context)) {
                    PermissionState.GRANTED_ALL
                } else {
                    PermissionState.GRANTED_FOREGROUND
                }
            }
            else -> PermissionState.DENIED
        }
    }

    fun startLocationTracking() {
        if (_permissionState.value == PermissionState.GRANTED_ALL ||
            _permissionState.value == PermissionState.GRANTED_FOREGROUND) {

            _locationState.value = LocationState.TRACKING
            val intent = Intent(context, LocationService::class.java)

            context.startForegroundService(intent)
        }
    }

    fun stopLocationTracking() {
        _locationState.value = LocationState.IDLE
        val intent = Intent(context, LocationService::class.java).apply {
            action = "STOP_TRACKING"
        }
        context.stopService(intent)
    }

    override fun onCleared() {
        LocalBroadcastManager.getInstance(context)
            .unregisterReceiver(locationReceiver)
        super.onCleared()
    }

    sealed class PermissionState {
        object UNKNOWN : PermissionState()
        object DENIED : PermissionState()
        object GRANTED_FOREGROUND : PermissionState()
        object GRANTED_ALL : PermissionState()
    }

    sealed class LocationState {
        object IDLE : LocationState()
        object TRACKING : LocationState()
        data class ERROR(val message: String) : LocationState()
    }
}