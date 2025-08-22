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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapptest.domain.models.AuthState
import com.example.weatherapptest.presentation.screens.CityDetailsScreen
import com.example.weatherapptest.presentation.screens.PhoneLoginScreen
import com.example.weatherapptest.presentation.screens.WeatherScreen
import com.example.weatherapptest.presentation.viewmodels.AuthViewModel
import com.example.weatherapptest.ui.theme.WeatherAppTestTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import com.example.weatherapptest.presentation.screens.OtpScreen


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
    val authState by authViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkAccount()
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.NoAccount -> navController.navigate("phoneLogin") {
                popUpTo(0)
            }
            is AuthState.LoggedIn -> navController.navigate("weatherScreen") {
                popUpTo(0)
            }
            is AuthState.WaitingOtp -> navController.navigate("otpScreen") {
                popUpTo(0)
            }
        }
    }

    NavHost(navController = navController, startDestination = "splashScreen") {
        composable("splashScreen") {}
        composable("phoneLogin") {
            PhoneLoginScreen(
                onLoginClick = { phone ->
                    authViewModel.addPhoneNumber(phone)
                }
            )
        }

        composable("otpScreen") {
            OtpScreen(
                onOtpEntered = { otp ->
                    authViewModel.verifyOtp(otp)
                }
            )
        }

        composable("weatherScreen") {
            WeatherScreen(navController)
        }

        composable("cityDetails/{cityName}") { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: ""
            CityDetailsScreen(cityName = cityName, navController = navController)
        }
    }
}

