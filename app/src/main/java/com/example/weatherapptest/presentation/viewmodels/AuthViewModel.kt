package com.example.weatherapptest.presentation.viewmodels

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.weatherapptest.R
import com.example.weatherapptest.domain.models.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    companion object {
        private const val SUPER_SECRET_CORRECT_CODE = "1234"

    }

    private val accountManager = AccountManager.get(context)
    private val accountType = context.getString(R.string.com_example_weatherapptest)


    fun checkAccount(): Flow<AuthState> = flow {
        val accounts = accountManager.getAccountsByType(accountType)
        if (accounts.isNotEmpty()) emit(AuthState.LoggedIn(accounts[0].name))
        else emit(AuthState.NoAccount)
    }


    fun verifyOtp(phone: String, otp: String): Flow<AuthState> = flow {
        if (otp == SUPER_SECRET_CORRECT_CODE) {
            val options = Bundle().apply { putString("phone", phone) }
            accountManager.addAccountExplicitly(
                Account(phone, accountType),
                null,
                options
            )
            emit(AuthState.LoggedIn(phone))
        } else {
            emit(AuthState.NoAccount)
        }
    }

    fun validatePhone(input: String): Boolean {
        val regex = Regex("^(\\+7|8)\\d{10}$")
        return regex.matches(input)
    }

}
