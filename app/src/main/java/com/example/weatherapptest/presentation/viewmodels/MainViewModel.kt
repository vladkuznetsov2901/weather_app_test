package com.example.weatherapptest.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _citiesWeather = MutableStateFlow<List<CurrentWeather>>(emptyList())
    val citiesWeather: StateFlow<List<CurrentWeather>> = _citiesWeather

    private val _cityWeatherByName =
        MutableStateFlow<CurrentWeather>(CurrentWeather("", 0.0, Coord(0.0, 0.0), ""))
    val cityWeatherByName: StateFlow<CurrentWeather> = _cityWeatherByName

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _citySuggestions = MutableStateFlow<List<City>>(emptyList())
    val citySuggestions: StateFlow<List<City>> = _citySuggestions

    private val _forecastByCity = MutableStateFlow<Forecast?>(null)
    val forecastByCity: StateFlow<Forecast?> = _forecastByCity

    private val baseCities = listOf("Moscow", "New York")

    fun getCitiesWeather() {
        viewModelScope.launch {
            _loading.value = true
            val citiesInfo = mutableListOf<CurrentWeather>()

            val userCities = getUserCitiesUseCase()

            val allCities = baseCities + userCities.map { it.name }

            for (city in allCities) {
                val result = getCurrentWeatherUseCase(city)
                when (result) {
                    is Resource.Success -> {
                        result.data.let { citiesInfo.add(it) }
                    }

                    is Resource.Error -> {
                        _error.value = result.message
                    }

                    is Resource.Loading -> {
                        _loading.value = true
                    }
                }
            }

            _citiesWeather.value = citiesInfo
            _loading.value = false
        }
    }

    fun getCityWeatherByName(cityName: String) {
        viewModelScope.launch {
            val result = getCurrentWeatherUseCase(cityName)
            if (result is Resource.Success) {
                _cityWeatherByName.value = result.data
                println("LOG: City loaded -> ${result.data}")
            } else {
                println("LOG: Failed to load city")
            }
        }
    }

    fun getForecastByCity(cityName: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            println("LOG: Requesting forecast for $cityName at ($lat, $lon)")
            val result = getForecastUseCase(cityName, lat, lon)
            when (result) {
                is Resource.Success -> {
                    println("LOG: Forecast loaded -> ${result.data}")
                    _forecastByCity.value = result.data
                }

                is Resource.Error -> {
                    println("LOG: Forecast error -> ${result.message}")
                }

                else -> {
                    println("LOG: Forecast unknown result -> $result")
                }
            }
        }
    }


    fun searchCities(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                val result = searchCityUseCase(query)
                when (result) {
                    is Resource.Success -> {
                        result.data.let { _citySuggestions.value = result.data }
                    }

                    is Resource.Error -> {
                        _error.value = result.message
                    }

                    is Resource.Loading -> {
                        _loading.value = true
                    }
                }

            } else {
                _citySuggestions.value = emptyList()
            }
        }
    }

    fun addCity(city: String) {
        viewModelScope.launch {
            addUserCityUseCase(city)
            getCitiesWeather()
        }
    }

}