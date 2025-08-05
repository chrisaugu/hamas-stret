package io.fantastix.hamasstret.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FareViewModel : ViewModel() {
    // TODO: Add state and business logic
    fun calculateFare(distanceKm: Double, baseRate: Double = 3.0, perKmRate: Double = 1.2): Double {
        return baseRate + (distanceKm * perKmRate)
    }
}
