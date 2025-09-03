package com.example.weatherapptest.domain.models

import android.os.Parcelable
import com.example.weatherapptest.data.data.Coord
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentWeather(
    val cityName: String,
    val temperature: Double,
    val coord: Coord,
    val description: String,

) : Parcelable
