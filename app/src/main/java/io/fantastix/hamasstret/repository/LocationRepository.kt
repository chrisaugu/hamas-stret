package io.fantastix.hamasstret.repository

import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import io.fantastix.hamasstret.model.LocationData
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.TimeUnit

class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _locationFlow = MutableStateFlow<LocationData?>(null)
    val locationFlow: StateFlow<LocationData?> = _locationFlow
    private val locationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation
    private val locationHistory = mutableListOf<Location>()
    private val MAX_HISTORY_SIZE = 100

    init {
        startLocationUpdates()
    }

    fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.SECONDS.toMillis(15)
        ).apply {
            setMinUpdateDistanceMeters(10f)
            setWaitForAccurateLocation(true)
            setDurationMillis(Long.MAX_VALUE)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.lastOrNull()?.let { location ->
                    _locationFlow.value = LocationData(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                }
            }
        }

        try {
            locationClient.requestLocationUpdates(
                locationRequest,
                locationCallback as LocationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("Location", "Permission denied", e)
        }
    }

    fun stopLocationUpdates() {
        locationCallback?.let {
            locationClient.removeLocationUpdates(it)
        }
    }

    suspend fun getLastKnownLocation(): LocationData? {
//        return try {
////            val location = LocationServices
////                .getFusedLocationProviderClient(context)
////                .lastLocation
////                      .await()
////                if (ActivityCompat.checkSelfPermission(
////                    context,
////                    Manifest.permission.ACCESS_FINE_LOCATION
////                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                    context,
////                    Manifest.permission.ACCESS_COARSE_LOCATION
////                ) != PackageManager.PERMISSION_GRANTED
////            ) {
////                // TODO: Consider calling
////                //    ActivityCompat#requestPermissions
////                // here to request the missing permissions, and then overriding
////                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                //                                          int[] grantResults)
////                // to handle the case where the user grants the permission. See the documentation
////                // for ActivityCompat#requestPermissions for more details.
////            } else {
////
////            }
//
////            location?.let {
////                LocationData(it.latitude, it.longitude)
////            }
//        } catch (e: Exception) {
//            null
//        }

        return LocationData(1.0, 0.2, 111L)
    }

    fun updateLocation(location: Location) {
        _currentLocation.value = location
        locationHistory.add(location)

        // Keep history size manageable
        if (locationHistory.size > MAX_HISTORY_SIZE) {
            locationHistory.removeAt(0)
        }
    }

    fun getLocationHistory(): List<Location> = locationHistory.toList()

    fun calculateDistance(start: Location, end: Location): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            start.latitude,
            start.longitude,
            end.latitude,
            end.longitude,
            results
        )
        return results[0]
    }
}