package io.fantastix.hamasstret.network

import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import io.fantastix.hamasstret.repository.FareCalculator
import io.fantastix.hamasstret.repository.TripRepository
import io.fantastix.hamasstret.repository.TripUpdate
import io.fantastix.hamasstret.ui.NotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LocationTrackingService : Service(), LocationListener {
    private lateinit var locationManager: LocationManager
    private lateinit var notificationManager: NotificationManager
    private var lastLocation: Location? = null
    private var totalDistance = 0.0
    private var startTime: Long = 0
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())
    private var isTripActive: Boolean = false

    override fun onCreate() {
        super.onCreate()

        notificationManager = NotificationManager(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        startForeground(1, notificationManager.createNotification("Starting trip..."))

        try {
            // Request GPS updates every 3 seconds or 5 meters
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000L,
                5f,
                this
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        // Background timer loop
        serviceScope.launch {
            while (isActive) {
                if (isTripActive) {
                    val elapsed = (System.currentTimeMillis() - startTime) / 1000
                    val fare = FareCalculator.calculateFare(totalDistance)
                    TripRepository.sendUpdate(
                        TripUpdate(fare, totalDistance / 1000.0, elapsed)
                    )
                }
                delay(1000)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        locationManager.removeUpdates(this)

        // Reset the FareCalculator when service is destroyed
        FareCalculator.reset()

        // Clear any ongoing trip data
        totalDistance = 0.0
        lastLocation = null
        startTime = 0
    }

    override fun onLocationChanged(location: Location) {
        if (lastLocation != null && isTripActive) {
            val distance = lastLocation!!.distanceTo(location)
            totalDistance += distance
        }
        lastLocation = location

        if (isTripActive) {
            val fare = FareCalculator.calculateFare(totalDistance / 1000)
            notificationManager.updateNotification("Fare: K %.2f | Distance: %.2f km".format(fare, totalDistance / 1000))
        } else {
            notificationManager.updateNotification("Trip paused - not tracking distance")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}