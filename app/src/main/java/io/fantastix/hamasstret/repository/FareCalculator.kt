package io.fantastix.hamasstret.repository

import android.location.Location
import io.fantastix.hamasstret.constants.Fares
import io.fantastix.hamasstret.model.FareResult
import io.fantastix.hamasstret.model.LocationData
import java.util.concurrent.TimeUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object FareCalculator {
    private var totalDistance = 0.0
    private var lastLat: Double? = null
    private var lastLon: Double? = null

    fun calculateFare(startLat: Double, startLon: Double, endLat: Double, endLon: Double): Double {
        val distanceMeters = calculateDistance(startLat, startLon, endLat, endLon)

        if (distanceMeters < Fares.MIN_DISTANCE_METERS) {
            return Fares.BASE_FARE
        }

//        val distanceKm = distanceMeters / 1000.0
//        return Fares.BASE_FARE + (distanceKm * Fares.COST_PER_KM)
        return calculateFare(distanceMeters)
    }

    fun calculateFare(start: LocationData, end: LocationData): FareResult {
        val distance = calculateDistance(start, end)
        val distanceFare = distance * Fares.COST_PER_KM
        val total = Fares.BASE_FARE + distanceFare

        return FareResult(
            distanceKm = distance,
            baseFare = Fares.BASE_FARE,
            distanceFare = distanceFare,
            totalFare = total,
            duration = calculateDuration(start.timestamp, end.timestamp)
        )
    }

    fun calculateFare(totalDistance: Double): Double {
        val distanceKm = totalDistance / 1000.0
        val fare = Fares.BASE_FARE + (distanceKm * Fares.COST_PER_KM)
        return fare
    }

    fun calculateFare(totalDistance: Float): Double {
        val distanceKm = totalDistance / 1000.0
        val fare = Fares.BASE_FARE + (distanceKm * Fares.COST_PER_KM)
        return fare
    }

    fun updateLocation(currentLat: Double, currentLon: Double): Double {
        if (lastLat != null && lastLon != null) {
            val result = FloatArray(1)
            Location.distanceBetween(
                lastLat!!,
                lastLon!!,
                currentLat,
                currentLon,
                result
            )
            totalDistance += result[0] // in meters
        }

        lastLat = currentLat
        lastLon = currentLon

//        val distanceKm = totalDistance / 1000.0
//        return Fares.BASE_FARE + (distanceKm * Fares.COST_PER_KM)
        return calculateFare(totalDistance)
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

    private fun calculateDistance(start: LocationData, end: LocationData): Double {
        // Haversine formula implementation
        val earthRadius = 6371 // Earth's radius in km
        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLon = Math.toRadians(end.longitude - start.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(start.latitude)) * cos(Math.toRadians(end.latitude)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    private fun calculateDuration(startTime: Long, endTime: Long): String {
        val durationMillis = endTime - startTime
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
        return "${minutes}min"
    }

    fun reset() {
        totalDistance = 0.0
        lastLat = null
        lastLon = null
    }
}