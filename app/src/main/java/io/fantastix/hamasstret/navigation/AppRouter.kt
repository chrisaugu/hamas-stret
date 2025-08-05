package io.fantastix.hamasstret.navigation

private object Route {
    const val LOGIN = "login"
    const val HOME = "home"
    const val ABOUT = "about"
    const val HISTORY = "history"
    const val RATES = "rates"
}

sealed class Screen(val route: String) {
    data object Login : Screen(Route.LOGIN)
    data object Home : Screen(Route.HOME)
    data object Rates : Screen(Route.RATES)
    data object History : Screen(Route.HISTORY)
    data object About : Screen(Route.ABOUT)
}
