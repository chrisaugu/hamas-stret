package io.fantastix.hamasstret.ui.screen.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.StreetViewPanoramaOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.streetview.StreetView
import com.google.maps.android.ktx.MapsExperimentalFeature
import io.fantastix.hamasstret.HamasStretApp
import io.fantastix.hamasstret.R
import io.fantastix.hamasstret.constants.TimerState
import io.fantastix.hamasstret.model.FareResult
import io.fantastix.hamasstret.model.LocationData
import io.fantastix.hamasstret.ui.LocationManager
import io.fantastix.hamasstret.ui.PermissionManager
import io.fantastix.hamasstret.ui.components.TribalButton
import io.fantastix.hamasstret.ui.screen.history.LocationHistoryList
import io.fantastix.hamasstret.ui.theme.Blue
import io.fantastix.hamasstret.ui.theme.HamasStretTheme
import io.fantastix.hamasstret.ui.theme.PngGold
import io.fantastix.hamasstret.ui.theme.PngRed
import io.fantastix.hamasstret.utils.PermissionUtils
import io.fantastix.hamasstret.viewmodel.FareViewModel
import io.fantastix.hamasstret.viewmodel.LocationSimulatorViewModel
import io.fantastix.hamasstret.viewmodel.LocationViewModel
import io.fantastix.hamasstret.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    companion object {
        private val TAG: String = "MainActivity"
    }
    private val mainViewModel: MainViewModel by viewModels()
    //    private lateinit var fareViewModel: FareViewModel
    private lateinit var context: Context
    private lateinit var permissionManager: PermissionManager

    @RequiresApi(Build.VERSION_CODES.S)
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
//        val mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

//        fareViewModel = FareViewModel(
//            FareCalculator,
//            locationRepository = LocationRepository(context)
//        )

        permissionManager = PermissionManager(context, locationPermissionRequest)
//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)

//        mainViewModel.showBackButton.value
        setContent {
            val navController = rememberNavController()

            HamasStretTheme {
//                AppNavHost(context)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            ),
                            title = { Text(context.getString(R.string.app_name)) },
                        )
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
//                        MapBottomSheet()
                        TimerScreen(mainViewModel)
//                        HomeScreen(mainViewModel)
                    }
                }
            }
        }
    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            viewModel.checkPermissions()
//
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                viewModel.startLocationTracking()
//            }
//        }
//    }
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, refresh UI
//                recreate()
//            }
//        }
//    }

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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this,
                "FCM can't post notifications without POST_NOTIFICATIONS permission",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun initializeApp() {
        val startTime = System.currentTimeMillis()

        // 1. Initialize app-wide preferences
        initPreferences()

        // 2. Setup crash reporting
        initCrashReporting()

        // 3. Check for critical dependencies
//        checkDependencies()

        // 4. Initialize analytics
        initAnalytics()

        // 5. Setup dependency injection (if not using Hilt)
//        initDependencyInjection()

        // 6. Check for first run
        checkFirstRun()

        // 7. Initialize logging
        initLogging()

        val duration = System.currentTimeMillis() - startTime
        Firebase.analytics.logEvent("init_duration") {
            param("duration_ms", duration.toLong())
        }
    }

    private fun initPreferences() {
        // Initialize SharedPreferences with proper mode
        val sharedPref = getSharedPreferences(
            "${packageName}_preferences",
            MODE_PRIVATE
        )

        // Set default values if not present
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)

        // Store in your application class or DI container
//        (application as `HamasStretApp).appPreferences = sharedPref

        lifecycleScope.launch {
            HamasStretApp.Companion.dataStorePreferences.saveUsername("john_doe")
            HamasStretApp.Companion.dataStorePreferences.saveDarkMode(true)
        }
    }

    private fun initCrashReporting() {
        // Example with Firebase Crashlytics
//        try {
//            FirebaseApp.initializeApp(this)
//            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
//
//            // Set user ID if available
//            val userId = getUserIdFromPrefs() // Implement your own method
//            userId?.let {
//                FirebaseCrashlytics.getInstance().setUserId(it)
//            }
//        } catch (e: Exception) {
//            Log.e("SplashActivity", "Failed to init crash reporting", e)
//        }
    }

    private fun initAnalytics() {
        // Firebase Analytics example
        val firebaseAnalytics = Firebase.analytics

        // Set user properties
        firebaseAnalytics.setUserProperty("app_version", BuildConfig.VERSION_NAME)
        firebaseAnalytics.setUserProperty("os_version", Build.VERSION.RELEASE)

        // Log app open event
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param(FirebaseAnalytics.Param.ORIGIN, "splash_screen")
        }

        // Store in application class
//        (application as HamasStretApp).analytics = firebaseAnalytics
    }

    private fun checkFirstRun() {
        val prefs = getSharedPreferences("${packageName}_preferences", MODE_PRIVATE)
        val isFirstRun = prefs.getBoolean("is_first_run", true)

        if (isFirstRun) {
            // Perform first-run initialization
//            initializeDefaultSettings()
//            downloadInitialData()

            // Mark as no longer first run
            prefs.edit().putBoolean("is_first_run", false).apply()

            // Log first run event
            Firebase.analytics.logEvent("first_run", null)
        }
    }

    private fun initLogging() {
        // Timber initialization
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree())
//        } else {
//            Timber.plant(CrashReportingTree())
//        }
//
//        // Example CrashReportingTree
//        private class CrashReportingTree : Timber.Tree() {
//            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//                if (priority == Log.ERROR || priority == Log.WARN) {
//                    FirebaseCrashlytics.getInstance().log("$tag: $message")
//                    t?.let { FirebaseCrashlytics.getInstance().recordException(it) }
//                }
//            }
//        }
    }

//    private suspend fun initializeApp() = withContext(Dispatchers.IO) {
//        val deferredList = mutableListOf<Deferred<Unit>>()
//
//        // 1. Start non-dependent initializations in parallel
//        deferredList += async { initPreferences() }
//        deferredList += async { initCrashReporting() }
//        deferredList += async { initLogging() }
//
//        // Wait for initial batch to complete
//        deferredList.awaitAll()
//        deferredList.clear()
//
//        // 2. Start dependent initializations
//        deferredList += async { checkDependencies() }
//        deferredList += async { initAnalytics() }
//
//        // 3. Wait for second batch
//        deferredList.awaitAll()
//
//        // 4. Final initializations
//        checkFirstRun()
//        initDependencyInjection()
//    }
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

@SuppressLint("UnrememberedMutableState")
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

    Box(Modifier.fillMaxSize()) {
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
fun TimerScreen(mainViewModel: MainViewModel) {
    val context = LocalContext.current

    val locationManager = remember { LocationManager(context) }
    val isTracking by mainViewModel.isTracking.collectAsState()
    val distance by mainViewModel.distance.collectAsState()
    val cost by mainViewModel.cost.collectAsState()
    val formattedTime by mainViewModel.formattedTime.collectAsState()
    val timerState = mainViewModel.timerState

//    val simulator = LocationSimulator(context)
//    simulator.enableTestProvider()

    // Simulate movement along a path
    val locations = listOf(
        LocationData(37.7749, -122.4194), // San Francisco
        LocationData(34.0522, -118.2437), // Los Angeles
        LocationData(40.7128, -74.0060)   // New York
    )

//    var index = 0
//    val handler = Handler(Looper.getMainLooper())
//    val runnable = object : Runnable {
//        override fun run() {
//            if (index < locations.size) {
//                val point = locations[index++]
//                simulator.simulateLocation(point.latitude, point.longitude)
//                handler.postDelayed(this, 5000) // Update every 5 seconds
//            }
//        }
//    }
//    handler.post(runnable)

    // Don't forget to cleanup
//    simulator.cleanup()

//    val uiState by mainViewModel.uiState.collectAsState(context)
//
//    when (uiState) {
//        is UiState.Loading -> LoadingView()
//        is UiState.Success -> DataView((uiState as UiState.Success).data)
//        is UiState.Error -> ErrorView((uiState as UiState.Error).message)
//    }

//    LaunchedEffect(isTracking) {
//        if (isTracking) {
//            locationManager.startTracking { location ->
//                if (location != null) {
//                    val lat = location.latitude
//                    val lon = location.longitude
//                    // update fare calculator here with lat/lon
////                    val fare = FareCalculator.calculateFare(
////                        startLat = -5.2190,
////                        startLon = 145.7892,
////                        endLat = -5.2141,
////                        endLon = 145.8010
////                    )
//                    val fare = FareCalculator.updateLocation(currentLat = lat, currentLon = lon)
////                    cost = fare
////                    mainViewModel.updateCost(fare)
//
//                    val meters = FareCalculator.calculateDistance(
//                        startLat = 0.0,
//                        startLon = 0.0,
//                        endLat = lat,
//                        endLon = lon
//                    )
////                    distance = meters / 1000.0
////                    cost = 3.40 + (distance * 4.40)
////                    mainViewModel.distance.value = (meters / 1000.0).toFloat()
//
//                    Log.d("TAG", "Total Fare: K %.2f".format(fare))
//                } else {
//                    // Handle GPS off or denied
//                    ActivityCompat.requestPermissions(
//                        context as MainActivity,
//                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                        101
//                    )
//                }
//            }
//
//            val startTime = System.currentTimeMillis()
//            while (isTracking) {
//                val elapsed = (System.currentTimeMillis() - startTime) / 1000
//                timerValue.inc()
//                val h = elapsed / 3600
//                val m = (elapsed % 3600) / 60
//                val s = elapsed % 60
//                formattedTime = String.format("%02d:%02d:%02d", h, m, s)
//                delay(1000)
//            }
//        } else {
//            locationManager.startTracking()
//        }
//
//        TripRepository.tripUpdates.collect { update ->
////            cost = update.fare
////            distance = update.distanceKm
//            val h = update.timeSec / 3600
//            val m = (update.timeSec % 3600) / 60
//            val s = update.timeSec % 60
//            formattedTime = String.format("%02d:%02d:%02d", h, m, s)
//        }
//    }
//
//    // Don’t forget to stop when leaving the screen
//    DisposableEffect(Unit) {
//        onDispose {
//            locationManager.startTracking()
//        }
//    }

    TimerScreenContent(
        state = timerState,
        timerValue = formattedTime,
        cost = cost,
        distance = distance,
        onStartClick = {
            mainViewModel.startTimer()
//            val intent = Intent(context, LocationTrackingService::class.java)
//            if (isTracking) {
//                context.stopService(intent)
//            } else {
//                context.startForegroundService(intent)
//            }
//            isTracking = !isTracking
        },
        onPauseClick = {
            mainViewModel.pauseTimer()
            // Stop the location tracking service when pausing
//            val intent = Intent(context, LocationTrackingService::class.java)
//            context.stopService(intent)
        },
        onResumeClick = {
            mainViewModel.resumeTimer()
            // Start the location tracking service when resuming
//            val intent = Intent(context, LocationTrackingService::class.java)
//            context.startForegroundService(intent)
        },
        onStopClick = {
            mainViewModel.stopTimer()
            // Also stop the location tracking service
//            val intent = Intent(context, LocationTrackingService::class.java)
//            context.stopService(intent)
        }
    )
}

@Composable
fun TimerScreenContent(
    state: TimerState,
    timerValue: String,
    cost: Double,
    distance: Double,
    onStartClick: () -> Unit,
    onResumeClick: () -> Unit,
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
            color = PngGold
        )

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatItem(label = "Time", value = timerValue, color = Blue)
            StatItem(label = "Distance", value = "${"%.2f".format(distance)} km", color = PngRed)
        }

        Spacer(Modifier.height(16.dp))

        // When the timer is not running, show start button
        // When the timer is running or paused, show two side-by-side buttons splitting the row
        when (state) {
            TimerState.STOPPED -> {
                TribalButton(
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    onClick = onStartClick,
                    text = "Start"
                )
            }
            TimerState.RUNNING -> {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    TribalButton(
                        modifier = Modifier.weight(1f).height(60.dp),
                        onClick = onPauseClick,
                        text ="Pause"
                    )
                    TribalButton(
                        modifier = Modifier.weight(1f).height(60.dp),
                        onClick = onStopClick,
                        text ="Stop"
                    )
                }
            }
            TimerState.PAUSED -> {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    TribalButton(
                        modifier = Modifier.weight(1f).height(60.dp),
                        onClick = onResumeClick,
                        text ="Resume"
                    )
                    TribalButton(
                        modifier = Modifier.weight(1f).height(60.dp),
                        onClick = onStopClick,
                        text ="Stop"
                    )
                }
            }
        }
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
//fun HomeScreen(
//    viewModel: FareViewModel = viewModel(factory = FareViewModelFactory())
//) {
fun HomeScreen(viewModel: FareViewModel) {
    // Check permission state
    val permissionState = viewModel.permissionState.value
//    val permissionState = PermissionUtils.createLocationPermissionRequest()
//    val permissionGranted = permissionState?.status?.isGranted : false
    val permissionGranted = viewModel.permissionState.value?.status?.isGranted ?: false
    val fallbackState by viewModel.fallbackState.collectAsState()
    var showFallbackOptions by remember { mutableStateOf(false) }

    LaunchedEffect(permissionState?.status) {
        if (permissionState?.status is PermissionStatus.Denied &&
            !showFallbackOptions &&
            fallbackState == FareViewModel.FallbackState.None
        ) {
            showFallbackOptions = true
        }
    }

    if (!permissionGranted) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Waiting for location permission...")
        }
    } else {
        val fareResult by viewModel.fareResult.collectAsState()
        val currentLocation by viewModel.currentLocation.collectAsState()
        val isTracking by viewModel.isTracking.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LocationDisplay(currentLocation)

            Spacer(modifier = Modifier.height(32.dp))

            FareDisplay(fareResult)

            Spacer(modifier = Modifier.weight(1f))

            LocationControls(
                isTracking = isTracking,
                onStart = { viewModel.startFareCalculation() },
                onStop = { viewModel.stopFareCalculation() },
                onReset = { viewModel.resetCalculation() },
                enabled = currentLocation != null,
                permissionState = viewModel.permissionState.value!!
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (fallbackState) {
            is FareViewModel.FallbackState.None -> {
                if (permissionGranted) {
                    OriginalHomeContent(viewModel)
                } else {
                    PermissionRequiredContent(
                        onRequestPermission = { permissionState?.launchPermissionRequest() }
                    )
                }
            }
            is FareViewModel.FallbackState.ManualEntry -> {
                ManualEntryScreen(
                    onLocationEntered = { location ->
                        viewModel.setManualLocation(location)
                    }
                )
            }
            is FareViewModel.FallbackState.EstimatedLocation -> {
                EstimatedLocationContent(
                    location = (fallbackState as FareViewModel.FallbackState.EstimatedLocation)
                        .lastKnownLocation,
                    viewModel = viewModel
                )
            }
        }

        if (showFallbackOptions) {
            FallbackOptions(
                viewModel = viewModel,
                onDismiss = { showFallbackOptions = false }
            )
        }
    }
}

@Composable
fun FareDisplay(fareResult: FareResult?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Fare Calculation",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (fareResult != null) {
                FareItem("Distance", "${"%.2f".format(fareResult.distanceKm)} km")
                FareItem("Duration", fareResult.duration)
                FareItem("Base Fare", "$${"%.2f".format(fareResult.baseFare)}")
                FareItem("Distance Fare", "$${"%.2f".format(fareResult.distanceFare)}")
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = DividerDefaults.Thickness,
                    color = DividerDefaults.color
                )
                FareItem("Total Fare", "$${"%.2f".format(fareResult.totalFare)}", true)
            } else {
                Text("Start ride to calculate fare")
            }
        }
    }
}

@Composable
fun FareItem(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(
            text = value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationControls(
    isTracking: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    enabled: Boolean,
    permissionState: PermissionState
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (!isTracking) {
            Button(
                onClick = onStart,
                enabled = enabled,
                modifier = Modifier.weight(1f)
            ) {
                Text("Start Ride")
            }
        } else {
            Button(
                onClick = onStop,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("End Ride")
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onReset,
            enabled = enabled,
            modifier = Modifier.weight(1f)
        ) {
            Text("Reset")
        }

//        Button(onClick = { permissionState.launchPermissionRequest })
    }

    fun handleStart() {
        if (permissionState.status.isGranted) {
            onStart()
        } else {
            Toast.makeText(
                context,
                "Location permission required",
                Toast.LENGTH_SHORT
            ).show()
            permissionState.launchPermissionRequest()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (!isTracking) {
            Button(
                onClick = ::handleStart,
                enabled = enabled,
                modifier = Modifier.weight(1f)
            ) {
                Text("Start Ride")
            }
        } else {
            // ... rest of the implementation ...
        }
    }
}

@Composable
fun OriginalHomeContent(viewModel: FareViewModel) {
    // Your original home screen content
}

@Composable
fun PermissionRequiredContent(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Location Permission Required",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("This app needs location access to calculate accurate fares")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Permission")
        }
    }
}

@Composable
fun EstimatedLocationContent(
    location: LocationData?,
    viewModel: FareViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Using Estimated Location", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                if (location != null) {
                    Text("Lat: ${"%.6f".format(location.latitude)}")
                    Text("Lng: ${"%.6f".format(location.longitude)}")
                    Text("Note: This might not be your current location")
                } else {
                    Text("No location data available")
                }
            }
        }

        Button(
            onClick = { viewModel.resetFallbackState() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Try Again With Location Access")
        }
    }
}

@Composable
fun ManualEntryScreen(
    onLocationEntered: (LocationData) -> Unit
) {
    var startAddress by remember { mutableStateOf("") }
    var endAddress by remember { mutableStateOf("") }
    var showMap by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Enter Ride Details", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = startAddress,
            onValueChange = { startAddress = it },
            label = { Text("Starting Address") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = endAddress,
            onValueChange = { endAddress = it },
            label = { Text("Destination Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { showMap = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select on Map")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // In a real app, you would geocode these addresses
                // For demo, we'll use dummy coordinates
                onLocationEntered(
                    LocationData(0.0, 0.0) // Replace with actual geocoded values
                )
            },
            enabled = startAddress.isNotBlank() && endAddress.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate Fare")
        }

//        if (showMap) {
//            MapSelectionDialog(
//                onLocationSelected = { lat, lng ->
//                    // Handle map selection
//                },
//                onDismiss = { showMap = false }
//            )
//        }
    }
}

@Composable
fun FallbackOptions(
    viewModel: FareViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Location Access Denied") },
        text = {
            Column {
                Text("This app works best with location access. Please choose an alternative:")
                Spacer(modifier = Modifier.height(16.dp))
                Text("1. Use last known location (if available)")
                Text("2. Enter addresses manually")
            }
        },
        confirmButton = {
            Column {
                Button(
                    onClick = {
                        viewModel.useLastKnownLocation()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Use Last Known Location")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.enableManualEntryFallback()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enter Manually")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Continue Without Location")
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(
    viewModel: FareViewModel,
    activity: Activity
) {
    val permissionState = createLocationPermissionRequest()

    LaunchedEffect(permissionState) {
        viewModel.initializePermissionState(permissionState)
        viewModel.checkLocationPermission(activity)
    }

    val showRationale by viewModel.showPermissionRationale.collectAsState()
    val permissionGranted = permissionState.status.isGranted

    if (!permissionGranted) {
        PermissionDeniedContent(
            showRationale = showRationale,
            onRequestPermission = { permissionState.launchPermissionRequest() }
        )
    }
}

@Composable
fun PermissionDeniedContent(
    showRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Don't allow dismiss without granting */ },
        title = {
            Text(text = "Location Permission Required")
        },
        text = {
            Text(
                text = if (showRationale) {
                    "This app needs location permission to calculate accurate fares. " +
                            "Please grant the permission to continue."
                } else {
                    "To calculate taxi fares based on distance, " +
                            "we need access to your location while using the app."
                }
            )
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Grant Permission")
            }
        },
        dismissButton = if (showRationale) {
            {
                TextButton(
                    onClick = { /* Handle denial */ }
                ) {
                    Text("Deny")
                }
            }
        } else null
    )
}

@OptIn(ExperimentalPermissionsApi::class)
class AppPermissionState(
    private val permissionState: PermissionState
) {
    val status: PermissionStatus
        get() = permissionState.status

    fun requestPermission() {
        permissionState.launchPermissionRequest()
    }

    fun shouldShowRationale(): Boolean {
        return (permissionState.status as? PermissionStatus.Denied)?.shouldShowRationale ?: false
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberAppPermissionState(permission: String): AppPermissionState {
    val permissionState = rememberPermissionState(permission)
    return remember { AppPermissionState(permissionState) }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionAwareLocationButton(
    permissionState: PermissionState,
    onLocationAvailable: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    Button(
        onClick = {
            when (permissionState.status) {
                is PermissionStatus.Granted -> onLocationAvailable()
                else -> {
                    permissionState.launchPermissionRequest()
                    onPermissionDenied()
                }
            }
        }
    ) {
        Text("Get Current Location")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CombinedPermissionHandler(
    locationPermission: PermissionState,
    cameraPermission: PermissionState
) {
    val allPermissionsGranted = locationPermission.status.isGranted && cameraPermission.status.isGranted

    LaunchedEffect(locationPermission.status, cameraPermission.status) {
        if (!allPermissionsGranted) {
            // Request all missing permissions at once
            if (!locationPermission.status.isGranted &&
                !cameraPermission.status.isGranted) {
                locationPermission.launchPermissionRequest()
                cameraPermission.launchPermissionRequest()
            }
        }
    }
}

//@Composable
//fun LocationTrackingScreen(viewModel: LocationViewModel = viewModel()) {
//    val permissionState by viewModel.permissionState.collectAsState()
//    val locationState by viewModel.locationState.collectAsState()
//    val currentLocation by LocationRepository.currentLocation.collectAsState()

//    Column(modifier = Modifier.padding(16.dp)) {
//        when (permissionState) {
//            is LocationViewModel.PermissionState.DENIED -> {
//                PermissionRequestView(
//                    onRequestPermission = {
//                        PermissionUtils.requestLocationPermission(
//                            LocalContext.current as Activity,
//                            LOCATION_PERMISSION_REQUEST_CODE
//                        )
//                    }
//                )
//            }
//            is LocationViewModel.PermissionState.GRANTED_FOREGROUND -> {
//                ForegroundOnlyWarning()
//                LocationControls(viewModel)
//            }
//            is LocationViewModel.PermissionState.GRANTED_ALL -> {
//                LocationControls(viewModel)
//            }
//            else -> { /* Loading or unknown state */ }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        when (locationState) {
//            is LocationViewModel.LocationState.TRACKING -> {
//                currentLocation?.let { location ->
//                    LocationDetails(location)
//                } ?: Text("Acquiring location...")
//            }
//            is LocationViewModel.LocationState.IDLE -> {
//                Text("Location tracking is inactive")
//            }
//            is LocationViewModel.LocationState.ERROR -> {
//                Text("Error: ${(locationState as LocationViewModel.LocationState.ERROR).message}")
//            }
//        }
//    }
//}

@Composable
fun LocationControls(viewModel: LocationViewModel) {
    val locationState by viewModel.locationState.collectAsState()

    Button(
        onClick = {
            if (locationState is LocationViewModel.LocationState.TRACKING) {
                viewModel.stopLocationTracking()
            } else {
                viewModel.startLocationTracking()
            }
        }
    ) {
        Text(
            if (locationState is LocationViewModel.LocationState.TRACKING)
                "Stop Tracking"
            else
                "Start Tracking"
        )
    }
}

@Composable
fun LocationDetails(location: Location) {
    Column {
        Text("Latitude: ${"%.6f".format(location.latitude)}")
        Text("Longitude: ${"%.6f".format(location.longitude)}")
        Text("Accuracy: ${"%.1f".format(location.accuracy)} meters")
        Text("Time: ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(location.time))}")
    }
}

@Composable
fun PermissionRationaleDialog(
    permissionText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Permission Required")
        },
        text = {
            Text("This app needs $permissionText permission to function properly. " +
                    "Please grant the permission to continue.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun createLocationPermissionRequest(): PermissionState {
    return rememberPermissionState(PermissionUtils.getRequiredPermissions()[0])
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun createLocationPermissionsRequest(): MultiplePermissionsState {
// For multiple permissions
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    return multiplePermissionsState
}

@Composable
fun LocationSimulationScreen(
    viewModel: LocationSimulatorViewModel = viewModel()
) {
    val currentLocation by viewModel.currentLocation.collectAsState()
    val isTracking by viewModel.isTracking.collectAsState()
    val simulationSpeed by viewModel.simulationSpeed.collectAsState()
    val locationHistory by remember { derivedStateOf { viewModel.locationHistory } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Current location display
        LocationDisplay(currentLocation)

        // Simulation controls
        SimulationControls(
            isTracking = isTracking,
            onStart = { viewModel.startSimulation() },
            onStop = { viewModel.stopSimulation() },
            speed = simulationSpeed,
            onSpeedChange = { viewModel.setSimulationSpeed(it) }
        )

        // Manual location input
        ManualLocationInput(
            onLocationSet = { lat, lon -> viewModel.setManualLocation(lat, lon) }
        )

        // Location history
        LocationHistoryList(locations = locationHistory)
    }
}

@Composable
fun LocationDisplay(location: LocationData?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Location",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (location != null) {
                Text(text = "Latitude: ${"%.6f".format(location.latitude)}")
                Text(text = "Longitude: ${"%.6f".format(location.longitude)}")
                Text(
                    text = "Updated: ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(location.timestamp))}",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text("No location data")
            }
        }
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Current Location",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (location != null) {
                Text(text = "Lat: ${"%.6f".format(location.latitude)}")
                Text(text = "Lng: ${"%.6f".format(location.longitude)}")
            } else {
                Text("Acquiring location...")
            }
        }
    }
}


@Composable
fun SimulationControls(
    isTracking: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    speed: Long,
    onSpeedChange: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Simulation Controls", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { if (isTracking) onStop() else onStart() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTracking) MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(if (isTracking) "Stop" else "Start")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Update Speed: ${speed}ms")
            Slider(
                value = speed.toFloat(),
                onValueChange = { onSpeedChange(it.toLong()) },
                valueRange = 200f..5000f,
                steps = 10
            )
        }
    }
}

@Composable
fun ManualLocationInput(onLocationSet: (Double, Double) -> Unit) {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Manual Location Input", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = latitude,
                onValueChange = { latitude = it },
                label = { Text("Latitude") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = longitude,
                onValueChange = { longitude = it },
                label = { Text("Longitude") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    latitude.toDoubleOrNull()?.let { lat ->
                        longitude.toDoubleOrNull()?.let { lon ->
                            onLocationSet(lat, lon)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = latitude.isNotBlank() && longitude.isNotBlank()
            ) {
                Text("Set Location")
            }
        }
    }
}