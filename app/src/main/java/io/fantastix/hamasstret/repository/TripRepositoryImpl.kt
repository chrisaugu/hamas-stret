package io.fantastix.hamasstret.repository

import io.fantastix.hamasstret.model.Trip
import io.fantastix.hamasstret.network.TripService

class TripRepositoryImpl() : ITripRepository {
    private val tripService: TripService
        get() {
            TODO()
        }

    // Overriding the interface method and providing implementation to it
    override suspend fun getTrips(): List<Trip> = listOf(
        Trip(1L, "10:10:10", 10.0, 25.0)
    )
//    override suspend fun getTrips(): List<Trip> {
//        return tripService.getTrips()
//    }

    override suspend fun createTrip(user: Trip): Trip {
        return tripService.createTrip(user)
    }

    override suspend fun getTrip(tripId: Int): Trip {
        return tripService.getTrip(tripId)
    }

    override suspend fun updateTrip(tripId: Int) {
        tripService.updateTrip(tripId)
    }

    override suspend fun deleteTrip(tripId: Int) {
        tripService.deleteTrip(tripId)
    }
}