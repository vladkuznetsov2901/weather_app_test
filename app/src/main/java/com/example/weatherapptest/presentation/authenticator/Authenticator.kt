package com.example.weatherapptest.presentation.authenticator

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle

class Authenticator(
    private val context: Context
) : AbstractAccountAuthenticator(context) {

    private val accountManager = AccountManager.get(context)

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        val phone = options?.getString("phone") ?: ""
        val account = Account(phone, accountType!!)
        accountManager.addAccountExplicitly(account, null, null)

        val result = Bundle()
        result.putString(AccountManager.KEY_ACCOUNT_NAME, phone)
        result.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType)
        return result
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ) = null

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?) = null
    override fun getAuthToken(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?) = null
    override fun getAuthTokenLabel(authTokenType: String?) = null
    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?) = null
    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?) = null
}
