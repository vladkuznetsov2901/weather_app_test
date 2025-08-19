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
    val pressure: Int,
    val humidity: Int,
    val wind: Wind
)

data class Wind(
    val speed: Double,       // скорость
    val deg: Long,           // направление в градусах
    val gust: Double?,       // порывы (опционально)
    val unit: String = "m/s" // единица измерения, можно "m/s" или "km/h"
)
