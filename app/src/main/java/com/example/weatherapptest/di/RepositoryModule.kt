package com.example.weatherapptest.di

import com.example.weatherapptest.data.db.CitiesDao
import com.example.weatherapptest.data.repository.CitiesRepositoryImpl
import com.example.weatherapptest.data.repository.WeatherRepositoryImpl
import com.example.weatherapptest.domain.repository.CitiesRepository
import com.example.weatherapptest.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideWeatherRepository() : WeatherRepository {
        return WeatherRepositoryImpl()
    }

    @Provides
    fun provideCitiesRepository(citiesDao: CitiesDao) : CitiesRepository {
        return CitiesRepositoryImpl(citiesDao)
    }

}