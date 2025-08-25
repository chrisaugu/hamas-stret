package io.fantastix.hamasstret.repository

import io.fantastix.hamasstret.model.Trip
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class TripUpdate(
    val fare: Double,
    val distanceKm: Double,
    val timeSec: Long
)

object TripRepository {
    private val _tripUpdates = MutableSharedFlow<TripUpdate>(replay = 1)
    val tripUpdates = _tripUpdates.asSharedFlow()

    suspend fun sendUpdate(update: TripUpdate) {
        _tripUpdates.emit(update)
    }
}

interface ITripRepository {
    suspend fun getTrips(): List<Trip>
    suspend fun createTrip(user: Trip): Trip
    suspend fun getTrip(tripId: Int): Trip
    suspend fun updateTrip(tripId: Int)
    suspend fun deleteTrip(tripId: Int)
}