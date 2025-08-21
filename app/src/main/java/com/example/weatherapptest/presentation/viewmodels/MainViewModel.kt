package com.example.weatherapptest.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
        MutableStateFlow(CurrentWeather("", 0.0, Coord(0.0, 0.0), ""))
    val cityWeatherByName: StateFlow<CurrentWeather> = _cityWeatherByName

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    private val _citySuggestions = MutableStateFlow<List<City>>(emptyList())
    val citySuggestions: StateFlow<List<City>> = _citySuggestions

    private val _forecastByCity = MutableStateFlow<Forecast?>(null)
    val forecastByCity: StateFlow<Forecast?> = _forecastByCity

    private val baseCities = listOf("Moscow", "New York")

    fun getCitiesWeather() {
        viewModelScope.launch {
            loading = true
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
                        error = result.message
                    }

                    is Resource.Loading -> {
                        loading = true
                    }
                }
            }

            _citiesWeather.value = citiesInfo
            loading = false
        }
    }

    fun getCityWeatherByName(cityName: String) {
        viewModelScope.launch {
            val result = getCurrentWeatherUseCase(cityName)
            if (result is Resource.Success) {
                _cityWeatherByName.value = result.data
                Log.d("LOG", "City loaded -> ${result.data}")

            } else {
                Log.d("LOG","LOG: Failed to load city")
            }
        }
    }

    fun getForecastByCity(cityName: String, lat: Double, lon: Double) {
        viewModelScope.launch {
            val result = getForecastUseCase(cityName, lat, lon)
            when (result) {
                is Resource.Success -> {
                    Log.d("LOG", "City loaded -> ${result.data}")
                    _forecastByCity.value = result.data
                }

                is Resource.Error -> {
                    Log.d("LOG","Forecast error -> ${result.message}")
                }

                else -> {
                    Log.d("LOG","Forecast unknown result -> $result")
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
                        error = result.message
                    }

                    is Resource.Loading -> {
                        loading = true
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