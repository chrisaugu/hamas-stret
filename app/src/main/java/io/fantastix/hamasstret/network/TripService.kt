package io.fantastix.hamasstret.network

import io.fantastix.hamasstret.model.Trip
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface TripService {
    @GET("trips")
    suspend fun getTrips(): List<Trip>

    @POST("trips")
    suspend fun createTrip(@Body trip: Trip): Trip

    @GET("trips/{tripId}")
    suspend fun getTrip(tripId: Int): Trip

    @PUT("trips/{tripId}")
    suspend fun updateTrip(tripId: Int)

    @DELETE("trips/{tripId}")
    suspend fun deleteTrip(tripId: Int)
}