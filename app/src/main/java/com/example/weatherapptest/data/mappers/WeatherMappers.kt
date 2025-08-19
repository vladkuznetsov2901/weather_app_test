package com.example.weatherapptest.data.mappers

import com.example.weatherapptest.data.models.CurrentWeatherDto
import com.example.weatherapptest.data.models.ForecastDto
import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.DailyForecast
import com.example.weatherapptest.domain.models.Forecast
import java.util.Date

fun CurrentWeatherDto.toDomain(): CurrentWeather {
    return CurrentWeather(
        cityName = name,
        temperature = main.temp,
        description = weather.firstOrNull()?.description.orEmpty(),
    )
}

fun ForecastDto.toDomain(cityName: String): Forecast {
    val dailyForecasts = daily.map { day ->
        DailyForecast(
            date = Date(day.dt * 1000),
            tempMin = day.temp.min,
            tempMax = day.temp.max,
            description = day.weather.firstOrNull()?.description.orEmpty()
        )
    }
    return Forecast(
        cityName = cityName,
        daily = dailyForecasts
    )
}