package com.example.weatherapptest.data.models

data class CurrentWeatherDto(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val coord: Coord
)

data class Main(
    val temp: Double
)

data class Weather(
    val description: String
)


data class Coord(
    val lat: Double,
    val lon: Double
)

