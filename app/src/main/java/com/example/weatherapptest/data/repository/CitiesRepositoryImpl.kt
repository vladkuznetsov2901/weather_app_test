package com.example.weatherapptest.data.repository

import com.example.weatherapptest.data.api.Api.Companion.retrofit
import com.example.weatherapptest.data.mappers.toDomain
import com.example.weatherapptest.data.models.CityDto
import com.example.weatherapptest.domain.models.City
import com.example.weatherapptest.domain.repository.CitiesRepository

class CitiesRepositoryImpl : CitiesRepository {
    override suspend fun searchCity(city: String): List<City> {
        val dto = retrofit.searchCities(city)
        return dto.toDomain()
    }
}