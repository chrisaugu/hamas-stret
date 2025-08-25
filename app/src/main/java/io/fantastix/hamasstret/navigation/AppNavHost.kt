package io.fantastix.hamasstret.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import io.fantastix.hamasstret.MainViewModel
import io.fantastix.hamasstret.ui.screen.about.AboutScreen
import io.fantastix.hamasstret.ui.screen.fare.FareScreen
import io.fantastix.hamasstret.ui.screen.history.HistoryScreen
import io.fantastix.hamasstret.ui.screen.home.HomeScreen

@Composable
fun AppNavHost(context: Context/*, navController: NavHostController = rememberNavController()*/) {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
//        composable("main") { it ->
//            val uiState by viewModel.uiState.collectAsState()
//
//            when (uiState) {
//                is UiState.Success<*> -> HomeScreen(onNavigate = { navController.navigate(it) })
//                else -> AuthScreen(onBack = { navController.navigate(it) })
//            }
//        }
        composable(Screen.Home.route) {
            HomeScreen(onNavigate = { navController.navigate(it) })
        }
        composable(Screen.Rates.route) {
            FareScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.History.route) {
            HistoryScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.About.route) {
            AboutScreen(context = context, onBack = { navController.popBackStack() })
        }
        dialog(
            route = Screen.History.route
        ) {
            FareScreen(onBack = { navController.navigate(it) })
        }
    }
}