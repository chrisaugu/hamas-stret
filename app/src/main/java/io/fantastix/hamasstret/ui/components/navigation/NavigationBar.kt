package io.fantastix.hamasstret.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*

@Composable
fun MYNavigationBar() {
    NavigationBar() {
        NavigationBarItem(
            selected = true,
            icon = { Icon(Icons.Filled.Add, null)},
            label = { Text("Item 1") },
            onClick = { /*TODO*/ }
        )
        NavigationBarItem(
            selected = false,
            icon = { Icon(Icons.Filled.Call, null)},
            label = { Text("Item 2") },
            onClick = { /*TODO*/ }
        )
        NavigationBarItem(
            selected = false,
            icon = { Icon(Icons.Filled.Notifications, null)},
            label = { Text("Item 3") },
            onClick = { /*TODO*/ }
        )
    }
}