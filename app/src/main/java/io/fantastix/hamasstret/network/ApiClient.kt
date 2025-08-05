package io.fantastix.hamasstret.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://nuku1-btlxx2lu.b4a.run/api/v2/stocks/"

    val tripService: TripService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TripService::class.java)
    }
}