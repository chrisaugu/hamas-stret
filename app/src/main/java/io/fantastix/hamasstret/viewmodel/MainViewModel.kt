package io.fantastix.hamasstret.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//@HiltViewModel
class MainViewModel(
//    private val emailsRepository: EmailsRepository = EmailsRepositoryImpl()
//    private val tripRepository: TripRepository = TripRepositoryImpl()
) : ViewModel() {
    private val _timer = MutableStateFlow(0L)
    private val _secondsRemaining = MutableStateFlow(0)
    private val _isTracking = MutableStateFlow(false)
    val timer = _timer.asStateFlow()
    val isTracking = _isTracking.asStateFlow()
    val secondsRemaining = _secondsRemaining.asStateFlow()

    private var timerJob: Job? = null

//    private val tripEmitter = MutableLiveData<List<Trip>>()
//    val trips: LiveData<List<Trip>> = tripEmitter
//    init {
//        loadTrips()
//    }

    private fun loadTrips() {
//        tripEmitter.value = tripRepository.getTrips()
    }

    fun startTimer() {
        _isTracking.value = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _timer.value++
            }
        }
    }

    fun pauseTimer() {
        _isTracking.value = false
        timerJob?.cancel()
    }

    fun stopTimer() {
        _isTracking.value = false
        _timer.value = 0
        timerJob?.cancel()
    }

    fun toggleTimer() {
        if (_isTracking.value) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

}