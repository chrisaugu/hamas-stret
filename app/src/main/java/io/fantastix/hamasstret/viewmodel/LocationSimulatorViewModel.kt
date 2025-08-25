package io.fantastix.hamasstret.viewmodel

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.fantastix.hamasstret.model.LocationData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LocationSimulatorViewModel : ViewModel() {
    // Location tracking state
    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking

    // Current simulated location
    private val _currentLocation = MutableStateFlow<LocationData?>(null)
    val currentLocation = _currentLocation

    // List of simulated locations
    private val _locationHistory = mutableStateListOf<LocationData>()
    val locationHistory: List<LocationData> = _locationHistory

    // Simulation speed (ms between updates)
    private val _simulationSpeed = MutableStateFlow(1000L)
    val simulationSpeed = _simulationSpeed

    private var simulationJob: Job? = null

    // Predefined routes for simulation
    private val predefinedRoutes = listOf(
        listOf(
            LocationData(37.7749, -122.4194), // San Francisco
            LocationData(34.0522, -118.2437),  // Los Angeles
            LocationData(40.7128, -74.0060)    // New York
        ),
        listOf(
            LocationData(51.5074, -0.1278),    // London
            LocationData(48.8566, 2.3522),     // Paris
            LocationData(55.7558, 37.6173)      // Moscow
        )
    )

    // Start location simulation
    fun startSimulation(routeIndex: Int = 0) {
        _isTracking.value = true
        _locationHistory.clear()

        simulationJob = viewModelScope.launch {
            val route = predefinedRoutes[routeIndex]
            var currentIndex = 0

            while (isTracking.value && currentIndex < route.size) {
                // Update current location
                _currentLocation.value = route[currentIndex]
                _locationHistory.add(route[currentIndex])

                // Move to next point or loop
                currentIndex = (currentIndex + 1) % route.size

                delay(simulationSpeed.value)
            }
        }
    }

    // Stop simulation
    fun stopSimulation() {
        _isTracking.value = false
        simulationJob?.cancel()
    }

    // Update simulation speed
    fun setSimulationSpeed(speed: Long) {
        _simulationSpeed.value = speed
        if (isTracking.value) {
            stopSimulation()
            startSimulation()
        }
    }

    // Set manual location
    fun setManualLocation(latitude: Double, longitude: Double) {
        val newLocation = LocationData(latitude, longitude)
        _currentLocation.value = newLocation
        _locationHistory.add(newLocation)
    }
}