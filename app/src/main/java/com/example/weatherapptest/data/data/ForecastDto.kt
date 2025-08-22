package com.example.weatherapptest.data.models

data class ForecastDto(
    val list: List<ForecastItemDto>,
    val city: CityDto
)

data class ForecastItemDto(
    val dt: Long,
    val main: MainDto,
    val weather: List<WeatherDto>,
    val wind: WindDto?,
    val dt_txt: String
)

data class MainDto(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class WeatherDto(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class WindDto(
    val speed: Double,
    val deg: Long,
    val gust: Double,
)
