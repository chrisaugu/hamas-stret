package io.fantastix.hamasstret.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import io.fantastix.hamasstret.ui.screen.about.AboutScreen
import io.fantastix.hamasstret.ui.screen.fare.FareScreen
import io.fantastix.hamasstret.ui.screen.history.HistoryScreen
import io.fantastix.hamasstret.ui.screen.home.HomeScreen

@Composable
fun RootNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
//            LoginScreen()
        }
        composable(Screen.Home.route) {
            HomeScreen(onNavigate = { navController.navigate(it) })
        }
        dialog(
            route = Screen.Login.route
        ) {
            FareScreen(onBack = { navController.navigate(it) })
        }
    }
}