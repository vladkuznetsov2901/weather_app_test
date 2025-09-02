package com.example.weatherapptest.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapptest.R
import com.example.weatherapptest.features.PhoneNumberVisualTransformation
import com.example.weatherapptest.presentation.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun PhoneLoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var phone by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.user_icon),
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = stringResource(R.string.sign_in_by_phone),
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = phone,
                onValueChange = { newValue ->
                    val digits = newValue.filter { it.isDigit() }


                    phone = digits.take(11)


                    isValid = authViewModel.validatePhone(phone)
                },
                visualTransformation = PhoneNumberVisualTransformation(),
                placeholder = { Text(stringResource(R.string.enter_the_phone)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (!isValid) {
                Text(
                    text = stringResource(R.string.enter_correct_phone),
                    color = androidx.compose.ui.graphics.Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    scope.launch {
                        if (isValid) {
                            navController.navigate("otpScreen/$phone") {
                                popUpTo("phoneLogin") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isValid
            ) {
                Text(stringResource(R.string.sign_in))
            }
        }
    }
}
