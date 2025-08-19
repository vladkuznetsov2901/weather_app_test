package com.example.weatherapptest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CityEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao

}