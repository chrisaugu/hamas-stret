package io.fantastix.hamasstret.viewmodel

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import io.fantastix.hamasstret.HamasStretApp
import io.fantastix.hamasstret.PermissionHandler
import io.fantastix.hamasstret.model.FareResult
import io.fantastix.hamasstret.model.LocationData
import io.fantastix.hamasstret.repository.FareCalculator
import io.fantastix.hamasstret.repository.LocationRepository
import io.fantastix.hamasstret.utils.PermissionUtils
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
class FareViewModel(
    private val fareCalculator: FareCalculator,
    private val locationRepository: LocationRepository
) : BaseVM() {
    private var startLocation: LocationData? = null
    private var manualStartLocation: LocationData? = null
    private var manualEndLocation: LocationData? = null
    private val _currentLocation = MutableStateFlow<LocationData?>(null)
    val currentLocation: StateFlow<LocationData?> = _currentLocation
    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking

    private val _fareResult = MutableStateFlow<FareResult?>(null)
    val fareResult: StateFlow<FareResult?> = _fareResult

    private val _permissionState = mutableStateOf<PermissionState?>(null)
    val permissionState: State<PermissionState?> = _permissionState

    private val _showPermissionRationale = MutableStateFlow(false)
    val showPermissionRationale: StateFlow<Boolean> = _showPermissionRationale

    private val _fallbackState = MutableStateFlow<FallbackState>(FallbackState.None)
    val fallbackState: StateFlow<FallbackState> = _fallbackState

    sealed class FallbackState {
        data object None : FallbackState()
        data object ManualEntry : FallbackState()
        data class EstimatedLocation(val lastKnownLocation: LocationData?) : FallbackState()
    }

    init {
        viewModelScope.launch {
            locationRepository.locationFlow.collect { location ->
                _currentLocation.value = location
                if (_isTracking.value && startLocation != null) {
                    _fareResult.value = fareCalculator.calculateFare(
                        startLocation!!,
                        location!!
                    )
                }
            }
        }
    }

    fun startFareCalculation() {
        _currentLocation.value?.let {
            startLocation = it
            _isTracking.value = true
        }
    }

    fun stopFareCalculation() {
        _isTracking.value = false
    }

    fun resetCalculation() {
        _isTracking.value = false
        _fareResult.value = null
        startLocation = null
    }

    fun resetFallbackState() {
        _fallbackState.value = FallbackState.None
        manualStartLocation = null
        manualEndLocation = null
    }

    fun calculateFare(distanceKm: Double, baseRate: Double = 3.0, perKmRate: Double = 1.2): Double {
        return baseRate + (distanceKm * perKmRate)
    }

    fun setManualLocation(location: LocationData) {
        if (manualStartLocation == null) {
            manualStartLocation = location
        } else {
            manualEndLocation = location
            calculateManualFare()
        }
    }

    private fun calculateManualFare() {
        manualStartLocation?.let { start ->
            manualEndLocation?.let { end ->
                _fareResult.value = fareCalculator.calculateFare(start, end)
            }
        }
    }

    fun enableManualEntryFallback() {
        _fallbackState.value = FallbackState.ManualEntry
    }

    fun useLastKnownLocation() {
        viewModelScope.launch {
            val lastLocation = locationRepository.getLastKnownLocation()
            _fallbackState.value = FallbackState.EstimatedLocation(lastLocation)
        }
    }

    fun initializePermissionState(permissionState: PermissionState) {
        _permissionState.value = permissionState
    }

    fun checkLocationPermission(activity: Activity) {
        _permissionState.value?.let { permissionState ->
            _showPermissionRationale.value = PermissionUtils.shouldShowRationale(
                activity,
                permissionState.permission
            )
        }
    }

    fun startLocationUpdates(context: Context) {
        viewModelScope.launch {
            try {
                PermissionUtils.startLocationUpdatesWithPermissionCheck(context)
            } catch (e: SecurityException) {
                _permissionState.value?.launchPermissionRequest()
            }
        }
    }

    // Define ViewModel factory in a companion object
//    companion object {
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//                extras: CreationExtras
//            ): T {
//                // Get the Application object from extras
//                val application = checkNotNull(extras[APPLICATION_KEY])
//                // Create a SavedStateHandle for this ViewModel from extras
//                val savedStateHandle = extras.createSavedStateHandle()
//
//                return FareViewModel(
//                    (application as HamasStretApp).myRepository,
//                    savedStateHandle
//                ) as T
//            }
//        }
//    }

    class FareViewModelFactory @Inject constructor(
        private val fareCalculator: FareCalculator,
        private val locationRepository: LocationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FareViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FareViewModel(fareCalculator, locationRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}