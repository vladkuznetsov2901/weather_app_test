package com.example.weatherapptest.domain.repository

import com.example.weatherapptest.data.models.CityDto
import com.example.weatherapptest.domain.models.City

interface CitiesRepository {

    suspend fun searchCity(city: String) : List<City>

}