package com.example.weatherapptest.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapptest.R
import com.example.weatherapptest.domain.models.AuthState
import com.example.weatherapptest.presentation.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(
    phone: String,
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()

) {
    val focusRequester = remember { FocusRequester() }
    var otpValue: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { focusRequester.requestFocus() }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            value = otpValue, onValueChange = { newValue ->
                val oldText = otpValue.text
                val newText = newValue.text
                when {
                    newText.length < oldText.length -> {
                        if (oldText.length > 3) {
                            otpValue = TextFieldValue(
                                text = oldText.dropLast(1),
                                selection = TextRange(oldText.length - 1)
                            )
                        }
                    }

                    newText.length > oldText.length -> {
                        if (oldText.length < 4) {
                            otpValue = TextFieldValue(
                                text = oldText + newText.last(),
                                selection = TextRange(oldText.length + 1)
                            )
                        }
                    }
                }
                if (newText.length == 4) {
                    scope.launch {
                        authViewModel.verifyOtp(phone, otpValue.text).collect { state ->
                            if (state is AuthState.LoggedIn) {
                                navController.navigate("weatherScreen") {
                                    popUpTo("otpScreen") { inclusive = true }
                                }
                            }
                        }
                    }
                }


            }, modifier = Modifier
                .size(1.dp)
                .focusRequester(focusRequester)
                .alpha(0f)
        )


        Text(stringResource(R.string.enter_code_from_sms), fontSize = 20.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(1.dp, Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = otpValue.text.getOrNull(index)?.toString() ?: "", fontSize = 20.sp
                    )
                }
            }
        }
    }
}

