package io.fantastix.hamasstret.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.fantastix.hamasstret.constants.TimerState
import io.fantastix.hamasstret.repository.FareCalculator
import io.fantastix.hamasstret.ui.NotificationManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

//@HiltViewModel
class MainViewModel(
    application: Application
) : AndroidViewModel(application), LocationListener {
    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()
    private var timerJob: Job? = null
    var elapsedTime by mutableLongStateOf(0L)
        private set
    private val _formattedTime = MutableStateFlow("0s")
    val formattedTime: StateFlow<String> = _formattedTime
    var timerState by mutableStateOf(TimerState.STOPPED)
        private set
    private var startTime: Long = 0L
    private val _uiState = MutableStateFlow<UiState<*>>(UiState.Loading)
    val uiState: StateFlow<UiState<*>> = _uiState.asStateFlow()
    private val _showBackButton = mutableStateOf(false)
    val showBackButton = _showBackButton
    private var _distance = MutableStateFlow(0.0)
    var distance = _distance.asStateFlow()
        private set
    private var _cost = MutableStateFlow(0.0)
    var cost = _cost.asStateFlow()
        private set
    //    private val dataStore = application.tripDataStore
    private var startCoords: Location? = null
    private var lastCoords: Location? = null
    private val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var isTripActive: Boolean = false
    private var notificationManager: NotificationManager = NotificationManager(application)

    fun updateBackButtonVisibility(shouldShow: Boolean) {
        _showBackButton.value = shouldShow
    }

//    private val tripEmitter = MutableLiveData<List<Trip>>()
//    val trips: LiveData<List<Trip>> = tripEmitter
//    init {
//        loadTrips()
//        loadData()
//        loadTripData()
//    }

    private fun loadData() {
//        viewModelScope.launch {
//            _uiState.value = UiState.Loading
//            _uiState.value = try {
//                UiState.Success(repository.fetchData())
//            } catch (e: Exception) {
//                UiState.Error(e.message)
//            }
//        }
//
//        _uiState.value = UiState.PartialLoading(
//            data = currentData,
//            isLoading = true
//        )
    }

    private fun loadTrips() {
//        tripEmitter.value = tripRepository.getTrips()
    }

    private fun loadTripData() {
//        viewModelScope.launch {
//            dataStore.data.collect { prefs ->
//                distanceTravelled = prefs[TripPrefs.DISTANCE] ?: 0.0
//                costInKina = prefs[TripPrefs.COST] ?: 0.0
//            }
//        }
    }

    private fun saveTripData() {
//        dataStore.edit { prefs ->
//            prefs[TripPrefs.DISTANCE] = distanceTravelled
//            prefs[TripPrefs.COST] = costInKina
//        }
    }

    fun startTimer() {
        if (timerState == TimerState.RUNNING) return

        timerState = TimerState.RUNNING
        _isTracking.value = true
        timerJob?.cancel()

        startLocationUpdates()

        timerJob = viewModelScope.launch {
            while (isActive) {
                _formattedTime.value = formatTime(elapsedTime)
                delay(1000)
                elapsedTime++
            }
        }
    }

    fun resumeTimer() {
        if (timerState == TimerState.PAUSED) {
            startTimer()
        }
    }

    fun pauseTimer() {
        if (timerState == TimerState.RUNNING) {
            timerState = TimerState.PAUSED
            _isTracking.value = false
            timerJob?.cancel()
            stopLocationUpdates()

            // Reset the FareCalculator when pausing to prevent continued calculation
            FareCalculator.reset()
        }
    }

    fun stopTimer() {
        timerState = TimerState.STOPPED
        _isTracking.value = false
        _formattedTime.value = "0s"
        elapsedTime = 0L
        stopLocationUpdates()
        timerJob?.cancel()

        // Reset distance and cost when stopping
        _distance.value = 0.0
        _cost.value = 0.0

        // Reset last coordinates to prevent distance calculation on next start
        lastCoords = null
        startCoords = null

        // Reset the FareCalculator to clear any accumulated distance
        FareCalculator.reset()
    }

    fun refresh() {
        loadData()
    }

    fun startTrip() {
        isTripActive = true
        startTime = System.currentTimeMillis()
        notificationManager.updateNotification("Trip started - tracking location...")
    }

    fun stopTrip() {
        isTripActive = false
        notificationManager.updateNotification("Trip stopped")
    }

    fun resetTrip() {
        _distance.value = 0.0
        _cost.value = 0.0
        elapsedTime = 0L
        _formattedTime.value = "0s"
        lastCoords = null
        startCoords = null
        stopLocationUpdates()

        // Reset the FareCalculator to clear any accumulated distance
        FareCalculator.reset()
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        stopLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            2000L,
            1f,
            this
        )
    }

    private fun stopLocationUpdates() {
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(location: Location) {
        // Only calculate distance and cost when timer is running
        if (timerState != TimerState.RUNNING) {
            lastCoords = location
            return
        }

        // Set starting point if not set yet
        if (startCoords == null) {
            startCoords = location
            lastCoords = location
            _distance.value = 0.0
            _cost.value = FareCalculator.calculateFare(0.0)
            return
        }

        // Distance from start point (meters)
        val fromStart = startCoords?.distanceTo(location)?.toDouble() ?: 0.0
        if (fromStart >= 0) {
            _distance.value = fromStart
            _cost.value = FareCalculator.calculateFare(fromStart)
//            viewModelScope.launch { saveTripData() }
        }
        lastCoords = location
    }

    private fun formatTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours != 0L) String.format("%2dh:%2dm:%2ds", hours, minutes, seconds)
        else if (minutes != 0L) String.format("%2dm:%2ds", minutes, seconds)
        else String.format("%2ds", seconds)
    }
}

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String? = null) : UiState<Nothing>
    data object Empty : UiState<Nothing>

    data class PartialLoading<T>(
        val data: T,
        val isLoading: Boolean,
        val error: String? = null
    ) : UiState<T>
}
