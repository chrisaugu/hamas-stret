package io.fantastix.hamasstret.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.fantastix.hamasstret.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    var isTracking by remember { mutableStateOf(false) }
    var fare by remember { mutableStateOf(2.00) }
    var distance by remember { mutableStateOf(0.0) }

    LaunchedEffect(isTracking) {
        while (isTracking) {
            delay(2000)
            distance += 0.05
            fare = 2.00 + (distance * 1.50)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Live Fare", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("PGK %.2f".format(fare), style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { isTracking = !isTracking }) {
            Text(if (isTracking) "Stop" else "Start")
        }
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { onNavigate(Screen.Rates.route) }) { Text("Rates") }
            Spacer(Modifier.width(16.dp))
            Button(onClick = { onNavigate(Screen.History.route) }) { Text("History") }
            Spacer(Modifier.width(16.dp))
            Button(onClick = { onNavigate(Screen.About.route) }) { Text("About") }
        }
    }
}
