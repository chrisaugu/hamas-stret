package io.fantastix.hamasstret.utils

import android.location.Location
import android.os.Handler
import android.os.Looper
import com.google.android.gms.maps.LocationSource

class MockLocationSource : LocationSource {
    private var listener: LocationSource.OnLocationChangedListener? = null

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        this.listener = listener
        // Start sending mock updates
        startMockUpdates()
    }

    override fun deactivate() {
        // Stop updates
        this.listener = null
    }

    private fun startMockUpdates() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            var count = 0
            override fun run() {
                val mockLocation = Location("mock").apply {
                    latitude = 37.7749 + (count * 0.001)
                    longitude = -122.4194 + (count * 0.001)
                    accuracy = 5f
                    time = System.currentTimeMillis()
                }
                listener?.onLocationChanged(mockLocation)
                count++
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }
}