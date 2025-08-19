package com.example.weatherapptest.domain.models

import com.example.weatherapptest.data.models.Coord

data class CurrentWeather(
    val cityName: String,
    val temperature: Double,
    val coord: Coord,
    val description: String,

)
