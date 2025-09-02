package com.example.weatherapptest.domain.usecases

import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.Resource
import com.example.weatherapptest.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(city: String): Flow<Resource<CurrentWeather>> = flow {
        emit(Resource.Loading())
        try {
            val data = weatherRepository.getCurrentWeather(city)
            emit(Resource.Success(data))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Неизвестная ошибка"))
        }
    }
}