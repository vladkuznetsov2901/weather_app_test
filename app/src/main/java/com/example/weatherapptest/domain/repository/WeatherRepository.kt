package com.example.weatherapptest.domain.repository

import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.Forecast

interface WeatherRepository {

    suspend fun getCurrentWeather(city: String): CurrentWeather
    suspend fun getForecast(lat: Double, lon: Double): Forecast

}