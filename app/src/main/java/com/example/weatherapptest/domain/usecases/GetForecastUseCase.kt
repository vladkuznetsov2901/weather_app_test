package com.example.weatherapptest.domain.usecases

import com.example.weatherapptest.domain.models.Forecast
import com.example.weatherapptest.domain.models.Resource
import com.example.weatherapptest.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    operator fun invoke(cityName: String, lat: Double?, lon: Double?): Flow<Resource<Forecast>> = flow {
        emit(Resource.Loading())
        try {
            val forecast = weatherRepository.getForecast(cityName, lat, lon)
            emit(Resource.Success(forecast))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
        }
    }


}