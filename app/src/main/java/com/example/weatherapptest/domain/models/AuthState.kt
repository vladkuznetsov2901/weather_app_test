package com.example.weatherapptest.domain.models

sealed class AuthState {
    object NoAccount : AuthState()
    data class WaitingOtp(val phone: String) : AuthState()
    data class LoggedIn(val phone: String) : AuthState()
}