package com.example.weatherapptest.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CityEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build().also { INSTANCE = it }
        }
    }

}