package com.example.weatherapptest.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.first
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

    val daysNumbers = listOf(3, 7)

    private val _citiesWeather = mutableStateListOf<CurrentWeather>()
    val citiesWeather: List<CurrentWeather> get() = _citiesWeather

    private val _weather = mutableStateOf<Resource<CurrentWeather>?>(null)
    val weather: State<Resource<CurrentWeather>?> get() = _weather

    private val _forecast = mutableStateOf<Resource<Forecast>?>(null)
    val forecast: State<Resource<Forecast>?> get() = _forecast

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

    suspend fun loadAllCities() {
        loading = true
        val userCitiesList = getUserCitiesUseCase().first()
        val allCities = baseCities + userCitiesList.map { it.name }
        val loadedCities = mutableListOf<CurrentWeather>()

        for (city in allCities) {
            getCurrentWeatherUseCase(city).collect { result ->
                when (result) {
                    is Resource.Success -> loadedCities.add(result.data)
                    is Resource.Error -> {
                        error = result.message
                    }

                    is Resource.Loading<*> -> {}
                }
            }
        }

        _citiesWeather.clear()
        _citiesWeather.addAll(loadedCities)

        loading = false
    }

    fun loadAllCitiesIfNeeded() {
        if (_citiesWeather.isEmpty()) {
            viewModelScope.launch {
                loadAllCities()
            }
        }
    }


    fun getCityWeatherByName(cityName: String) {
        Log.d("WeatherViewModel", "getCityWeatherByName() called with cityName=$cityName")
        viewModelScope.launch {
            getCurrentWeatherUseCase(cityName).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _weather.value = Resource.Success(result.data)
                        Log.d("WeatherViewModel", "Weather loaded successfully: ${result.data}")
                    }

                    is Resource.Error -> {
                        _weather.value = Resource.Error(result.message)
                        Log.d("WeatherViewModel", "Weather loading error: ${result.message}")
                    }

                    is Resource.Loading -> {
                        _weather.value = Resource.Loading()
                        Log.d("WeatherViewModel", "Weather loading in progress...")
                    }
                }
            }
        }
    }


    fun getForecastByCity(cityName: String, lat: Double?, lon: Double?) {
        viewModelScope.launch {
            getForecastUseCase(cityName, lat, lon).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _forecast.value = Resource.Success(result.data)
                        Log.d("WeatherViewModel", "Forecast loaded successfully: ${result.data}")
                    }

                    is Resource.Error -> {
                        _forecast.value = Resource.Error(result.message)
                        Log.d("WeatherViewModel", "Forecast loading error: ${result.message}")
                    }

                    is Resource.Loading -> {
                        _forecast.value = Resource.Loading()
                        Log.d("WeatherViewModel", "Forecast loading in progress...")
                    }
                }
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

            getCurrentWeatherUseCase(newCity).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _citiesWeather.add(result.data)
                    }

                    is Resource.Error -> error = result.message
                    is Resource.Loading<*> -> {}
                }
            }
        }
    }
}