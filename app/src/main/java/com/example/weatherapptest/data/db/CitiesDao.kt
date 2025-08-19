package com.example.weatherapptest.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapptest.domain.models.City

@Dao
interface CitiesDao {

    @Query("SELECT * FROM cities")
    suspend fun getUserCities(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)
}