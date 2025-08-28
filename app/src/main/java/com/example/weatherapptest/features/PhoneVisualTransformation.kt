package com.example.weatherapptest.features

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }
        val formatted = buildString {
            if (digits.isNotEmpty()) append("+7 ")
            if (digits.length > 1) {
                append("(")
                append(digits.substring(1, minOf(4, digits.length)))
            }
            if (digits.length >= 4) {
                append(") ")
                append(digits.substring(4, minOf(7, digits.length)))
            }
            if (digits.length >= 7) {
                append("-")
                append(digits.substring(7, minOf(9, digits.length)))
            }
            if (digits.length >= 9) {
                append("-")
                append(digits.substring(9, minOf(11, digits.length)))
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 0 -> 0
                    offset == 1 -> 3
                    offset in 2..4 -> offset + 3
                    offset in 5..7 -> offset + 5
                    offset in 8..9 -> offset + 6
                    offset >= 10 -> minOf(formatted.length, offset + 7)
                    else -> formatted.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return digits.length.coerceAtMost(offset)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}
