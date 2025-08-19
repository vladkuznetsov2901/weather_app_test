package com.example.weatherapptest.domain.usecases

import com.example.weatherapptest.domain.repository.CitiesRepository
import javax.inject.Inject

class AddUserCityUseCase @Inject constructor(
    private val citiesRepository: CitiesRepository
) {

    suspend operator fun invoke(city: String) {

        citiesRepository.addUserCity(city)

    }
}