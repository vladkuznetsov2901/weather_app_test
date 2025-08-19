package com.example.weatherapptest.data.models

data class ForecastDto(
    val daily: List<Daily>
)

data class Daily(
    val dt: Long,
    val temp: Temp,
    val weather: List<Weather>
)

data class Temp(
    val day: Double,
    val min: Double,
    val max: Double
)

