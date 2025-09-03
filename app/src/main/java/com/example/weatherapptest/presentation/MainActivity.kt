package com.example.weatherapptest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapptest.domain.models.AuthState
import com.example.weatherapptest.presentation.screens.CityDetailsScreen
import com.example.weatherapptest.presentation.screens.OtpScreen
import com.example.weatherapptest.presentation.screens.PhoneLoginScreen
import com.example.weatherapptest.presentation.screens.WeatherScreen
import com.example.weatherapptest.presentation.viewmodels.AuthViewModel
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
fun AppContent(authViewModel: AuthViewModel = hiltViewModel()) {

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        authViewModel.checkAccount().collect { authState ->
            when (authState) {
                is AuthState.NoAccount -> navController.navigate("phoneLogin") { popUpTo(0) }
                is AuthState.LoggedIn -> navController.navigate("weatherScreen") { popUpTo(0) }
                is AuthState.WaitingOtp -> navController.navigate("otpScreen/${authState.phone}") {
                    popUpTo(
                        0
                    )
                }
            }
        }
    }



    NavHost(navController = navController, startDestination = "splashScreen") {
        composable("splashScreen") {}
        composable("phoneLogin") {
            PhoneLoginScreen(navController = navController)
        }
        composable(
            route = "otpScreen/{phone}",
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpScreen(phone = phone, navController = navController)
        }
        composable("weatherScreen") {
            WeatherScreen(navController)
        }

        composable("cityDetails") { backStackEntry ->
            CityDetailsScreen(navController = navController)
        }
    }
}

