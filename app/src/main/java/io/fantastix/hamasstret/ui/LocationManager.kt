package io.fantastix.hamasstret.ui

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationManager(private val context: Context) {
    private var locationManager: LocationManager? = null
    private var locationCallback: ((Location?) -> Unit)? = null
//    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    private var locationCallbackFn: LocationCallback? = null

    var startLocation: Location? = null
    var totalDistanceMeters: Float = 0f

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("LocationManager", "Location updated: ${location.latitude}, ${location.longitude}")
            locationCallback?.invoke(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {
            locationCallback?.invoke(null)
        }
    }

    fun startTracking(callback: (Location?) -> Unit) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationCallback = callback

        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            Log.w("LocationManager", "Location permission not granted.")
            callback(null)
            return
        }

        try {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000L,   // 2 seconds
                1f,      // 1 meter
                locationListener
            )
        } catch (e: Exception) {
            Log.e("LocationManager", "Error requesting location updates", e)
            callback(null)
        }
    }

    fun startTracking() {
        locationManager?.removeUpdates(locationListener)
        locationCallback = null
    }
    
//    @SuppressLint("MissingPermission")
//    fun startTracking(onLocationUpdate: (Float) -> Unit) {
//        val request = LocationRequest.Builder(
//            Priority.PRIORITY_HIGH_ACCURACY, 2000L // every 2 seconds
//        ).build()
//
//        locationCallback = object : LocationCallback(), (Location?) -> Unit {
//            override fun onLocationResult(result: LocationResult) {
//                val newLocation = result.lastLocation ?: return
//
//                if (startLocation == null) {
//                    startLocation = newLocation
//                } else {
//                    val distance = FloatArray(1)
//                    Location.distanceBetween(
//                        startLocation!!.latitude,
//                        startLocation!!.longitude,
//                        newLocation.latitude,
//                        newLocation.longitude,
//                        distance
//                    )
//
//                    totalDistanceMeters = distance[0]
//                    onLocationUpdate(totalDistanceMeters)
//                }
//            }
//
//            override fun invoke(p1: Location?) {
//                TODO("Not yet implemented")
//            }
//        }
//
//        fusedLocationClient.requestLocationUpdates(request, locationCallback!!, Looper.getMainLooper())
//    }
//
//    fun stopTracking() {
//        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
//    }
}
