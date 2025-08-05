package io.fantastix.hamasstret.ui.screen.fare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FareScreen(onBack: () -> Unit) {
    var baseFare by remember { mutableStateOf("2.00") }
    var perKmRate by remember { mutableStateOf("1.50") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Customize Rates", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = baseFare, onValueChange = { baseFare = it }, label = { Text("Base Fare") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = perKmRate, onValueChange = { perKmRate = it }, label = { Text("Per KM Rate") })
        Spacer(Modifier.height(24.dp))
        Button(onClick = onBack) { Text("Save & Back") }
    }
}
