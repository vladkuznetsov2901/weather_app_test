package com.example.weatherapptest.domain.models

sealed class AuthState {
    object NoAccount : AuthState()
    object WaitingOtp : AuthState()
    data class LoggedIn(val phone: String) : AuthState()
}