package io.fantastix.hamasstret.repository

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat
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
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())

    override fun onCreate() {
        super.onCreate()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                val fare = 3.40 + (totalDistance / 1000 * 4.40)
                TripRepository.sendUpdate(
                    TripUpdate(fare, totalDistance / 1000.0, elapsed)
                )
                delay(1000)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(content: String): Notification {
        val channelId = "trip_channel"
        val channelName = "Trip Tracking"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Hamas Stret")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(content: String) {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = createNotification(content)
        nm.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        if (lastLocation != null) {
            val distance = lastLocation!!.distanceTo(location)
            totalDistance += distance
        }
        lastLocation = location

        val fare = 3.40 + (totalDistance / 1000 * 4.40)
        updateNotification("Fare: K %.2f | Distance: %.2f km".format(fare, totalDistance / 1000))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}
