package com.example.weatherapptest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapptest.presentation.screens.CityDetailsScreen
import com.example.weatherapptest.presentation.screens.WeatherScreen
import com.example.weatherapptest.ui.theme.WeatherAppTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTestTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "museumsList") {
        composable("museumsList") {
            WeatherScreen(navController)
        }
        composable("cityDetails/{cityName}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            CityDetailsScreen(cityName = cityName, navController = navController)
        }
    }
}

