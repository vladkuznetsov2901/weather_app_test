package com.example.weatherapptest.data

import com.example.weatherapptest.data.models.CurrentWeatherDto
import com.example.weatherapptest.data.models.ForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): CurrentWeatherDto

    @GET("data/2.5/onecall")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "minutely,hourly,alerts",
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): ForecastDto

    private companion object {
        private val API_KEY = "4153b10d9256fb388c24a24e0b8f7a40"
    }
}