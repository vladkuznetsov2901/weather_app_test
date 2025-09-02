package com.example.weatherapptest.data.api

import com.example.weatherapptest.data.models.CityDto
import com.example.weatherapptest.data.data.CurrentWeatherDto
import com.example.weatherapptest.data.models.ForecastDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): CurrentWeatherDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): ForecastDto

    @GET("geo/1.0/direct")
    suspend fun searchCities(
        @Query("q") city: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String = API_KEY
    ): List<CityDto>

    companion object {
        private const val API_KEY = "4153b10d9256fb388c24a24e0b8f7a40"

        val retrofit: Api by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }


}