package io.fantastix.hamasstret

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.ktx.MapsExperimentalFeature
import io.fantastix.hamasstret.repository.FareCalculator
import io.fantastix.hamasstret.repository.LocationTrackingService
import io.fantastix.hamasstret.repository.TripRepository
import io.fantastix.hamasstret.ui.components.TribalButton
import io.fantastix.hamasstret.ui.theme.HamasStretTheme
import io.fantastix.hamasstret.ui.LocationManager
import io.fantastix.hamasstret.ui.PermissionManager
import io.fantastix.hamasstret.viewmodel.MainViewModel
import io.fantastix.hamasstret.viewmodel.TripViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    companion object {
        private val TAG: String = "MainActivity"
    }
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var tripViewModel: TripViewModel
    private lateinit var context: Context
    private lateinit var permissionManager: PermissionManager

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {
                // No location access granted.
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
//        edgeToEdgeWithStyle()
        super.onCreate(savedInstanceState)

        installSplashScreen()
//            .setKeepOnScreenCondition {
//            someCondition
//        }

        context = applicationContext
//        val repository = TripRepository(ApiClient.tripService)
//
//        val viewModelFactory = TripViewModelFactory(repository)
//        tripViewModel = ViewModelProvider(this, viewModelFactory).get(TripViewModel::class.java)
//        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//
//        // Observe trips LiveData
//        tripViewModel.trips.observe(this, Observer { trips ->
//            // Update UI with the list of trips
//        })
//
//        // Fetch trips
//        tripViewModel.getTrips()

        permissionManager = PermissionManager(context, locationPermissionRequest)
//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)

        setContent {
            var showDialog by remember { mutableStateOf(false) }

            HamasStretTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = {
                                Text(getString(context, R.string.app_name))
                            }
                        )
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        TimerScreenContent(mainViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Default Preview Dark"
)
@Composable
fun GreetingPreview() {
    HamasStretTheme {
        Greeting("Android")
//        TimerScreenContent()
        MapBottomSheet()
    }
}

@OptIn(MapsExperimentalFeature::class)
@Composable
fun MapScreen() {
    val atasehir = LatLng(40.9971, 29.1007)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(atasehir, 15f)
    }
    val uiSettings by remember {
        mutableStateOf(MapUiSettings(zoomControlsEnabled = true))
    }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.SATELLITE))
    }

    val routeCoordinates = listOf(
        LatLng(40.9967,29.0570), // Starting point
        LatLng(40.9900,30.0570), // Waypoint 1
        LatLng(41.0322,29.0216), // Waypoint 2
        LatLng(41.0333,29.0910)  // Ending point
    )

    StreetView(
        streetViewPanoramaOptionsFactory = {
            StreetViewPanoramaOptions().position(atasehir)
        },
        isPanningGesturesEnabled = true,
        isStreetNamesEnabled = true,
        isUserNavigationEnabled = true,
        isZoomGesturesEnabled = true
    )

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        Marker(
            state = MarkerState(position = atasehir),
            title = "One Marker"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapBottomSheet() {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TribalButton(
            isPrimary = true,
            onClick = { showBottomSheet = true },
            text = "Display partial bottom sheet"
        )

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                Text(
                    "Swipe up to open sheet. Swipe down to dismiss.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun TimerScreenContent(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val locationManager = remember { LocationManager(context) }
    val timerValue by mainViewModel.timer.collectAsState()
    var timeElapsed by remember { mutableLongStateOf(0L) }
    val isTracking by mainViewModel.isTracking.collectAsState()
    var distance by remember { mutableDoubleStateOf(0.0) }
    var cost by remember { mutableDoubleStateOf(0.0) }
    var formattedTime by remember { mutableStateOf("00:00:00") }

    LaunchedEffect(isTracking) {
        if (isTracking) {
            locationManager.startTracking { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    // update fare calculator here with lat/lon
//                    val fare = FareCalculator.calculateFare(
//                        startLat = -5.2190,
//                        startLon = 145.7892,
//                        endLat = -5.2141,
//                        endLon = 145.8010
//                    )
                    val fare = FareCalculator.updateLocation(currentLat = lat, currentLon = lon)
                    cost = fare

                    val meters = FareCalculator.calculateDistance(
                        startLat = 0.0,
                        startLon = 0.0,
                        endLat = lat,
                        endLon = lon
                    )
                    distance = meters / 1000.0
//                    cost = 3.40 + (distance * 4.40)

                    Log.d("TAG", "Total Fare: K %.2f".format(fare))
                } else {
                    // Handle GPS off or denied
                    ActivityCompat.requestPermissions(
                        context as MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        101
                    )
                }
            }

            val startTime = System.currentTimeMillis()
            while (isTracking) {
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                timeElapsed += 1
                val h = elapsed / 3600
                val m = (elapsed % 3600) / 60
                val s = elapsed % 60
                formattedTime = String.format("%02d:%02d:%02d", h, m, s)
                delay(1000)
            }
        } else {
            locationManager.startTracking()
        }

        TripRepository.tripUpdates.collect { update ->
            cost = update.fare
            distance = update.distanceKm
            val h = update.timeSec / 3600
            val m = (update.timeSec % 3600) / 60
            val s = update.timeSec % 60
            formattedTime = String.format("%02d:%02d:%02d", h, m, s)
        }
    }

    // Don’t forget to stop when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            locationManager.startTracking()
        }
    }

    TimerScreen(
        timerValue = timerValue,
        timeElapsed = timeElapsed,
        cost = cost,
        distance = distance,
        isTracking = isTracking,
        onToggle = { mainViewModel.toggleTimer() },
        onStartClick = {
            mainViewModel.startTimer()
            val intent = Intent(context, LocationTrackingService::class.java)
            if (isTracking) {
                context.stopService(intent)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
//            isTracking = !isTracking
        },
        onPauseClick = { mainViewModel.pauseTimer() },
        onStopClick = { mainViewModel.stopTimer() }
    )
}

@Composable
fun TimerScreen(
    timerValue: Long,
    timeElapsed: Long,
    cost: Double,
    distance: Double,
    isTracking: Boolean,
    onToggle: () -> Unit,
    onStartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
//        Text(text = timerValue.formatTime(), fontSize = 24.sp)

//        Card(
//            colors = CardDefaults.cardColors(
//                containerColor =
//                if (isSelected) MaterialTheme.colorScheme.primaryContainer
//                else
//                    MaterialTheme.colorScheme.surfaceVariant
//            ),
//            modifier = Modifier
//                .padding(24.dp)
//                .fillMaxWidth(),
//        ) {
//            Text(
//                text = "Dinner club",
//                style = MaterialTheme.typography.bodyLarge,
//                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
//                else MaterialTheme.colorScheme.onSurface,
//            )
//        }

//        Greeting(
//            name = "Android",
//            modifier = Modifier
//                .fillMaxWidth(),
//        )

//        Text(
//            text = "Cost: K${String.format("%.2f", cost)}",
//            style = MaterialTheme.typography.headlineSmall
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(text = "Time: ${timeElapsed}s", style = MaterialTheme.typography.headlineMedium)
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "Dist: ${String.format("%.2f", distance)} km",
//                style = MaterialTheme.typography.headlineSmall
//            )
//        }
        Spacer(modifier = Modifier.height(16.dp))
//        Spacer(modifier = Modifier.height(16.dp))
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            TribalButton(onClick = onStartClick, text = "Start")
//            Spacer(modifier = Modifier.width(16.dp))
//            TribalButton(onClick = onPauseClick, text = "Pause")
//            Spacer(modifier = Modifier.width(16.dp))
//            TribalButton(onClick = onStopClick, text = "Stop")
//        }

        Text(
            text = "Current Fare",
            style = MaterialTheme.typography.titleMedium,
            color = Color.LightGray
        )
        Text(
            text = "K ${"%.2f".format(cost)}",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color(0xFFFFD700) // PNG gold
        )

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatItem(label = "Time", value = "${timeElapsed}s", color = Color(0xFF00BFFF))
            StatItem(label = "Distance", value = "${"%.2f".format(distance)} km", color = Color(0xFFFF6347))
        }

        Spacer(Modifier.height(16.dp))

        TribalButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            onClick = onStartClick,
            text = if (isTracking) "Stop" else "Start"
        )

//            MapBottomSheet()
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}