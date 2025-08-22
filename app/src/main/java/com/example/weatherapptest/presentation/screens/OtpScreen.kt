package com.example.weatherapptest.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.weatherapptest.R

@Composable
fun OtpScreen(
    onOtpEntered: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var otpValue: TextFieldValue by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                if (newText.length == 4) onOtpEntered(otpValue.text)


            }, modifier = Modifier
                .size(1.dp)
                .focusRequester(focusRequester)
                .alpha(0f)
        )

        LaunchedEffect(Unit) { focusRequester.requestFocus() }

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

