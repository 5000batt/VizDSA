package com.kjw.vizdsa.navigation

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object DataStructure : Screen("data_structure")
    object Algorithm : Screen("algorithm")
    object Array : Screen("array")
}