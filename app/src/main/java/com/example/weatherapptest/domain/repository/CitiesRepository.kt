package com.example.weatherapptest.domain.repository

import com.example.weatherapptest.data.db.CityEntity
import com.example.weatherapptest.domain.models.City
import kotlinx.coroutines.flow.Flow

interface CitiesRepository {

    suspend fun searchCity(city: String) : List<City>

    suspend fun addUserCity(city: String)

    fun getUserCities(): Flow<List<CityEntity>>

}