package io.fantastix.hamasstret.network

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
import io.fantastix.hamasstret.R
import io.fantastix.hamasstret.repository.FareCalculator
import io.fantastix.hamasstret.repository.TripRepository
import io.fantastix.hamasstret.repository.TripUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LocationTrackingService : Service(), LocationListener {

    private lateinit var locationManager: LocationManager
    private var lastLocation: Location? = null
    private var totalDistance: Float = 0f
    private var startTime: Long = 0
    private var isTripActive: Boolean = false
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        startForeground(1, createNotification("Starting trip..."))

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
    
    fun startTrip() {
        isTripActive = true
        startTime = System.currentTimeMillis()
        updateNotification("Trip started - tracking location...")
    }
    
    fun stopTrip() {
        isTripActive = false
        updateNotification("Trip stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(content: String): Notification {
        val channelId = "trip_channel"
        val channelName = "Trip Tracking"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Hamas Stret")
            .setContentText(content)
//            .setSmallIcon(R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(content: String) {
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = createNotification(content)
        nm.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        locationManager.removeUpdates(this)
        
        // Reset the FareCalculator when service is destroyed
        io.fantastix.hamasstret.repository.FareCalculator.reset()
        
        // Clear any ongoing trip data
        totalDistance = 0f
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
            updateNotification("Fare: K %.2f | Distance: %.2f km".format(fare, totalDistance / 1000))
        } else {
            updateNotification("Trip paused - not tracking distance")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}