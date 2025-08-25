package io.fantastix.hamasstret.model

data class FareResult(
    val distanceKm: Double,
    val baseFare: Double,
    val distanceFare: Double,
    val totalFare: Double,
    val duration: String
)