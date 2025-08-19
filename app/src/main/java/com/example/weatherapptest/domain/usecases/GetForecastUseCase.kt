package com.example.weatherapptest.domain.usecases

import com.example.weatherapptest.domain.models.Forecast
import com.example.weatherapptest.domain.models.Resource
import com.example.weatherapptest.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(cityName: String, lat: Double, lon: Double): Resource<Forecast> {
        return try {
            val data = weatherRepository.getForecast(cityName, lat, lon)
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Неизвестная ошибка")
        }
    }

}