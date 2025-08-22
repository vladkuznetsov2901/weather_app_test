package com.example.weatherapptest.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapptest.data.db.AppDatabase
import com.example.weatherapptest.data.db.CitiesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Volatile
    private var instance: AppDatabase? = null
    private val lock = Any()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return instance ?: synchronized(lock) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build().also { instance = it }
        }
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): CitiesDao {
        return appDatabase.citiesDao()
    }
}