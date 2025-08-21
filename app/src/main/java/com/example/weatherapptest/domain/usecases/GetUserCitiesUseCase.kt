package com.example.weatherapptest.domain.usecases

import com.example.weatherapptest.data.db.CityEntity
import com.example.weatherapptest.domain.repository.CitiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserCitiesUseCase @Inject constructor(
    private val citiesRepository: CitiesRepository
) {

    operator fun invoke(): Flow<List<CityEntity>> {
        return citiesRepository.getUserCities()
    }

}