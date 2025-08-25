package io.fantastix.hamasstret.navigation

private object Route {
    const val HOME = "home"
    const val ABOUT = "about"
    const val HISTORY = "history"
    const val RATES = "rates"
}

sealed class Screen(val route: String) {
    data object Home : Screen(Route.HOME)
    data object Rates : Screen(Route.RATES)
    data object History : Screen(Route.HISTORY)
    data object About : Screen(Route.ABOUT)
}
