package io.fantastix.hamasstret.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.fantastix.hamasstret.model.Trip
import io.fantastix.hamasstret.repository.ITripRepository
import kotlinx.coroutines.launch

class TripViewModel(private val tripRepository: ITripRepository) : ViewModel() {

    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> get() = _trips

    // Fetch the list of trips from the repository
    fun getTrips() {
        viewModelScope.launch {
            _trips.value = tripRepository.getTrips()
        }
    }
    // Create a new trip
    fun createTrip(trip: Trip) {
        viewModelScope.launch {
            val newTrip = tripRepository.createTrip(trip)
            // Handle the created trip here if needed
        }
    }
}

class TripViewModelFactory(private val tripRepository: ITripRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(tripRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}