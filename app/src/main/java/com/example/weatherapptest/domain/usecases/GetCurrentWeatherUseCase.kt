package com.example.weatherapptest.domain.usecases

import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.Resource
import com.example.weatherapptest.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(city: String): Resource<CurrentWeather> {
        return try {
            val data = weatherRepository.getCurrentWeather(city)
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Неизвестная ошибка")
        }
    }
}