package com.example.weatherapptest.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
) : Parcelable
