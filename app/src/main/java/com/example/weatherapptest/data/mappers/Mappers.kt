package com.example.weatherapptest.data.mappers

import com.example.weatherapptest.data.models.CityDto
import com.example.weatherapptest.data.models.CurrentWeatherDto
import com.example.weatherapptest.data.models.ForecastDto
import com.example.weatherapptest.domain.models.City
import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.DailyForecast
import com.example.weatherapptest.domain.models.Forecast
import com.example.weatherapptest.domain.models.Wind
import java.util.Calendar

fun CurrentWeatherDto.toDomain(): CurrentWeather {
    return CurrentWeather(
        cityName = name,
        temperature = main.temp,
        coord = coord,
        description = weather.firstOrNull()?.description.orEmpty(),
    )
}

fun ForecastDto.toDomain(cityName: String): Forecast {
    val groupedByDay = list.groupBy { item ->
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = item.dt * 1000
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.time
    }

    val dailyForecasts = groupedByDay.map { (date, items) ->
        val tempMin = items.minOf { it.main.temp_min }
        val tempMax = items.maxOf { it.main.temp_max }
        val description = items.firstOrNull()?.weather?.firstOrNull()?.description.orEmpty()
        val pressure = items.map { it.main.pressure }.average().toInt()
        val humidity = items.map { it.main.humidity }.average().toInt()
        val windSpeed = items.mapNotNull { it.wind?.speed }.average()
        val windDeg = items.mapNotNull { it.wind?.deg }.average().toLong()
        val windGust = items.mapNotNull { it.wind?.gust }.average()

        val wind = Wind(
            speed = windSpeed,
            deg = windDeg,
            gust = windGust,
            unit = "m/s"
        )

        DailyForecast(
            date = date,
            tempMin = tempMin,
            tempMax = tempMax,
            description = description,
            pressure = pressure,
            humidity = humidity,
            wind = wind
        )
    }.sortedBy { it.date.time }

    return Forecast(
        cityName = cityName,
        daily = dailyForecasts
    )
}


fun CityDto.toDomain(): City {
    return City(
        name = name,
        lon = lon,
        lat = lat,
        country = country
    )
}

fun List<CityDto>.toDomain(): List<City> {
    return map { it.toDomain() }
}