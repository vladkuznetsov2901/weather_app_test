package com.example.weatherapptest.presentation.viewmodels

import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.weatherapptest.R
import com.example.weatherapptest.domain.models.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val accountManager = AccountManager.get(context)
    private val accountType = context.getString(R.string.com_example_weatherapptest)
    val superSecretCorrectCode = "1234"

    private val _state = MutableStateFlow<AuthState>(AuthState.NoAccount)
    val state: StateFlow<AuthState> = _state

    fun checkAccount() {
        val accounts = accountManager.getAccountsByType(accountType)
        _state.value = if (accounts.isNotEmpty()) {
            AuthState.LoggedIn(accounts[0].name)
        } else {
            AuthState.NoAccount
        }
    }

    fun addPhoneNumber(phone: String) {
        val options = Bundle().apply {
            putString("phone", phone)
        }

        accountManager.addAccount(
            accountType,
            null,
            null,
            options,
            null,
            { future ->
                try {
                    future.result
                    _state.value = AuthState.WaitingOtp
                } catch (_: Exception) {
                    _state.value = AuthState.NoAccount
                }
            },
            null
        )
    }

    fun verifyOtp(otp: String) {
        if (otp == superSecretCorrectCode) {
            val accounts = accountManager.getAccountsByType(accountType)
            _state.value = AuthState.LoggedIn(accounts.firstOrNull()?.name ?: "")
        }
    }
}
