package com.example.weatherapptest.domain.usecases

import com.example.weatherapptest.domain.models.City
import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.Resource
import com.example.weatherapptest.domain.repository.CitiesRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val citiesRepository: CitiesRepository
) {

    suspend operator fun invoke(city: String): Resource<List<City>> {
        return try {
            val data = citiesRepository.searchCity(city)
            Resource.Success(data)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Неизвестная ошибка")
        }
    }

}