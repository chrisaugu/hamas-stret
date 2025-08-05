package io.fantastix.hamasstret.repository

import android.location.Location
import io.fantastix.hamasstret.constants.Fares

object FareCalculator {
    private var totalDistance = 0.0
    private var lastLat: Double? = null
    private var lastLon: Double? = null

    fun calculateFare(startLat: Double, startLon: Double, endLat: Double, endLon: Double): Double {
        val distanceMeters = calculateDistance(startLat, startLon, endLat, endLon)

        if (distanceMeters < Fares.MIN_DISTANCE_METERS) {
            return Fares.BASE_FARE
        }

        val distanceKm = distanceMeters / 1000.0
        return Fares.BASE_FARE + (distanceKm * Fares.COST_PER_KM)
    }

    fun updateLocation(currentLat: Double, currentLon: Double): Double {
        if (lastLat != null && lastLon != null) {
            val result = FloatArray(1)
            Location.distanceBetween(lastLat!!, lastLon!!, currentLat, currentLon, result)
            totalDistance += result[0] // in meters
        }

        lastLat = currentLat
        lastLon = currentLon

        val distanceKm = totalDistance / 1000.0
        return Fares.BASE_FARE + (distanceKm * Fares.COST_PER_KM)
    }

    /**
     * Calculates the distance between two points in kilometers.
     * @return Distance in meters.
     */
    fun calculateDistance(startLat: Double, startLon: Double, endLat: Double, endLon: Double): Float {
        val result = FloatArray(1)
        Location.distanceBetween(startLat, startLon, endLat, endLon, result)
        val totalDistance = result[0] // in meters

        return totalDistance
    }

    fun reset() {
        totalDistance = 0.0
        lastLat = null
        lastLon = null
    }
}