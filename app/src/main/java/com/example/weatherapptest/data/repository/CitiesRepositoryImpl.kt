package com.example.weatherapptest.data.repository

import com.example.weatherapptest.data.api.Api.Companion.retrofit
import com.example.weatherapptest.data.db.CitiesDao
import com.example.weatherapptest.data.db.CityEntity
import com.example.weatherapptest.data.mappers.toDomain
import com.example.weatherapptest.domain.models.City
import com.example.weatherapptest.domain.repository.CitiesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CitiesRepositoryImpl @Inject constructor(
    private val citiesDao: CitiesDao
) : CitiesRepository {

    private val lock = Any()


    override suspend fun searchCity(city: String): List<City> {
        val dto = retrofit.searchCities(city)
        return dto.toDomain()
    }

    override suspend fun addUserCity(city: String) {
        val cityEntity = CityEntity(name = city)
        synchronized(lock) {
            citiesDao.insertCity(cityEntity)
        }
    }

    override fun getUserCities(): Flow<List<CityEntity>> {
        return synchronized(lock) { citiesDao.getUserCities() }
    }
}