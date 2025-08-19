package com.example.weatherapptest.data.repository

import com.example.weatherapptest.data.Api.Companion.retrofit
import com.example.weatherapptest.data.mappers.toDomain
import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.Forecast
import com.example.weatherapptest.domain.repository.WeatherRepository

class WeatherRepositoryImpl : WeatherRepository {
    override suspend fun getCurrentWeather(city: String): CurrentWeather {
        val dto = retrofit.getCurrentWeather(city)
        return dto.toDomain()
    }

    override suspend fun getForecast(cityName: String, lat: Double, lon: Double): Forecast {
        val dto = retrofit.getForecast(lat, lon)
        return dto.toDomain(cityName)
    }
}