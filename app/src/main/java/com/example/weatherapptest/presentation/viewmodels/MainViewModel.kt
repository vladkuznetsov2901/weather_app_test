package com.example.weatherapptest.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptest.data.db.CityEntity
import com.example.weatherapptest.data.models.Coord
import com.example.weatherapptest.domain.models.City
import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.Forecast
import com.example.weatherapptest.domain.models.Resource
import com.example.weatherapptest.domain.usecases.AddUserCityUseCase
import com.example.weatherapptest.domain.usecases.GetCurrentWeatherUseCase
import com.example.weatherapptest.domain.usecases.GetForecastUseCase
import com.example.weatherapptest.domain.usecases.GetUserCitiesUseCase
import com.example.weatherapptest.domain.usecases.SearchCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val searchCityUseCase: SearchCityUseCase,
    private val getUserCitiesUseCase: GetUserCitiesUseCase,
    private val addUserCityUseCase: AddUserCityUseCase
) : ViewModel() {

    var citiesWeather by mutableStateOf<List<CurrentWeather>>(emptyList())
        private set


    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var suggestions by mutableStateOf<List<City>>(emptyList())
        private set


    private val baseCities = listOf("Moscow", "New York")


    init {
        viewModelScope.launch {
            loadAllCities()
        }
    }

    private suspend fun loadAllCities() {
        loading = true
        val userCitiesList = getUserCitiesUseCase().first()
        val allCities = baseCities + userCitiesList.map { it.name }
        val loadedCities = mutableListOf<CurrentWeather>()

        for (city in allCities) {
            when (val result = getCurrentWeatherUseCase(city)) {
                is Resource.Success -> loadedCities.add(result.data)
                is Resource.Error -> {
                    error = result.message
                }

                is Resource.Loading<*> -> {}
            }
        }
        citiesWeather = loadedCities
        loading = false
    }


    fun getCityWeatherByName(cityName: String): Flow<CurrentWeather> = flow {
        emit(CurrentWeather("", 0.0, Coord(0.0, 0.0), ""))
        when (val result = getCurrentWeatherUseCase(cityName)) {
            is Resource.Success -> emit(result.data)
            is Resource.Error -> {
                error = result.message
            }

            is Resource.Loading<*> -> {
                loading = true
            }
        }
    }


    fun getForecastByCity(cityName: String, lat: Double, lon: Double): Flow<Forecast?> = flow {
        emit(null)
        when (val result = getForecastUseCase(cityName, lat, lon)) {
            is Resource.Success -> {
                Log.d("LOG", "City loaded -> ${result.data}")
                emit(result.data)
            }

            is Resource.Error -> {
                Log.d("LOG", "Forecast error -> ${result.message}")
            }

            else -> {
                Log.d("LOG", "Forecast unknown result -> $result")
            }
        }
    }


    fun searchCities(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                when (val result = searchCityUseCase(query)) {
                    is Resource.Success -> {
                        suggestions = result.data
                    }

                    is Resource.Error -> {
                        suggestions = emptyList()
                    }

                    is Resource.Loading -> {}
                }
            } else {
                suggestions = emptyList()
            }
        }
    }


    fun addCity(newCity: String) {
        viewModelScope.launch {
            addUserCityUseCase(newCity)

            when (val result = getCurrentWeatherUseCase(newCity)) {
                is Resource.Success -> {
                    citiesWeather = citiesWeather + result.data
                }
                is Resource.Error -> error = result.message
                is Resource.Loading<*> -> {}
            }
        }
    }
}