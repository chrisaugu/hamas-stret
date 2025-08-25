package io.fantastix.hamasstret.utils

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import androidx.annotation.RequiresApi

class LocationSimulator(private val context: Context) {
    private val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
    private val mockProviderName = "test_provider"

    @RequiresApi(Build.VERSION_CODES.S)
    fun enableTestProvider() {
        // Check if mock location is enabled in developer options
        if (Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ALLOW_MOCK_LOCATION,
                0
            ) != 1
        ) {
            throw SecurityException("Mock location not enabled in developer options")
        }

        // Add test provider
        locationManager.addTestProvider(
            mockProviderName,
            false, // requiresNetwork
            false, // requiresSatellite
            false, // requiresCell
            false, // hasMonetaryCost
            true,  // supportsAltitude
            true,  // supportsSpeed
            true,  // supportsBearing
            ProviderProperties.POWER_USAGE_LOW,
            ProviderProperties.ACCURACY_FINE
        )

        // Enable the test provider
        locationManager.setTestProviderEnabled(mockProviderName, true)
    }

    fun simulateLocation(latitude: Double, longitude: Double) {
        val mockLocation = Location(mockProviderName).apply {
            this.latitude = latitude
            this.longitude = longitude
            this.accuracy = 5f
            this.time = System.currentTimeMillis()
            this.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
            this.speed = 0f
            this.bearing = 0f
            this.altitude = 0.0
        }

        locationManager.setTestProviderLocation(mockProviderName, mockLocation)
    }

    fun cleanup() {
        try {
            locationManager.removeTestProvider(mockProviderName)
        } catch (e: Exception) {
            // Provider not registered
        }
    }
}