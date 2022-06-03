package at.technikum_wien.polzert.news.view

sealed class Screen (val route : String) {
    object MainScreen : Screen(route = "main_screen")
    object DetailScreen : Screen(route = "detail_screen")
    object SettingsScreen : Screen(route = "settings_screen")
}
