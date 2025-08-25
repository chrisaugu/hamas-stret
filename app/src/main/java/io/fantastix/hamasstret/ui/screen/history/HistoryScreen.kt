package io.fantastix.hamasstret.ui.screen.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.fantastix.hamasstret.network.ApiClient

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
