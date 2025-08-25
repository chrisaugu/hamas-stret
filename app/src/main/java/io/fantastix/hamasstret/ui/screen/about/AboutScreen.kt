package io.fantastix.hamasstret.ui.screen.about

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.fantastix.hamasstret.R
import io.fantastix.hamasstret.ui.components.navigation.Appbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(context: Context, onBack: () -> Unit) {
    Scaffold(
        topBar = { Appbar(context, onBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(16.dp))

            // App name
            Text("Hamas Stret", style =  MaterialTheme.typography.headlineLarge)

            // Version number
            Text("Version 1.0.0", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))

            // Short description
            Text(
                text = "Hamas Stret helps you calculate taxi fares in Papua New Guinea. " +
                        "Simple, fast, and accurate for your daily travels.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(16.dp)) {
                    TextButton(onClick = { /* TODO: Open Privacy Policy */ }) {
                        Text("Privacy Policy")
                    }
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    TextButton(onClick = { /* TODO: Open Terms of Service */ }) {
                        Text("Terms of Service")
                    }
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    TextButton(onClick = { /* TODO: Open Help */ }) {
                        Text("Help")
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}