package io.fantastix.hamasstret.ui.screen.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.fantastix.hamasstret.model.LocationData
import io.fantastix.hamasstret.network.ApiClient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(onBack: () -> Unit) {
    val items = (1..100).toList()

//    val repository = TripRepository(ApiClient.tripService)
//
//    val viewModelFactory = TripViewModelFactory(repository)
//    tripViewModel = ViewModelProvider(this, viewModelFactory).get(TripViewModel::class.java)
//    // Observe trips LiveData
//    tripViewModel.trips.observe(this, Observer { trips ->
//        // Update UI with the list of trips
//    })
//
//    // Fetch trips
//    tripViewModel.getTrips()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Trip History", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("No trips yet...", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Back") }

        LazyColumn {
            items(items) { item ->
                Text(
                    text = "Item $item",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun LocationHistoryList(locations: List<LocationData>) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Location History", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.Info else Icons.Default.Clear,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {
                    items(locations.reversed()) { location ->
                        LocationHistoryItem(location = location)
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationHistoryItem(location: LocationData) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = "${"%.6f".format(location.latitude)}, ${"%.6f".format(location.longitude)}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(location.timestamp)),
            style = MaterialTheme.typography.bodySmall
        )
    }
}