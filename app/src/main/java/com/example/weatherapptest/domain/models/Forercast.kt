package com.example.weatherapptest.domain.models

import java.util.Date

data class Forecast(
    val cityName: String,
    val daily: List<DailyForecast>
)

data class DailyForecast(
    val date: Date,
    val tempMin: Double,
    val tempMax: Double,
    val description: String,
)