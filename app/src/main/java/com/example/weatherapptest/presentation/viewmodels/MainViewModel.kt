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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
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

    val userCities: Flow<List<CityEntity>> = getUserCitiesUseCase()

    val citiesWeather: Flow<Resource<List<CurrentWeather>>> = getCitiesWeatherFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = Resource.Loading()
        )


    val cityWeatherByName: Flow<CurrentWeather> = getCityWeatherByName("")

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    val forecast: Flow<Forecast?>
        get() = getForecastByCity(cityName = "", lat = 0.0, lon = 0.0)

    val citySuggestions: Flow<List<City>>
        get() = searchCities(query = "")


    private val baseCities = listOf("Moscow", "New York")

    fun getCitiesWeatherFlow(): Flow<Resource<List<CurrentWeather>>> = flow {
        emit(Resource.Loading())

        val citiesList = userCities.first()
        val allCities = baseCities + citiesList.map { it.name }

        val citiesInfo = mutableListOf<CurrentWeather>()
        for (city in allCities) {
            when (val result = getCurrentWeatherUseCase(city)) {
                is Resource.Success -> citiesInfo.add(result.data)
                is Resource.Error -> emit(Resource.Error(result.message))
                is Resource.Loading<*> -> {}
            }
        }

        emit(Resource.Success(citiesInfo))
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
                error = result.message
            }
            else -> {
                Log.d("LOG", "Forecast unknown result -> $result")
            }
        }
    }


    fun searchCities(query: String): Flow<List<City>> = flow {
        if (query.isNotBlank()) {
            when (val result = searchCityUseCase(query)) {
                is Resource.Success -> emit(result.data)
                is Resource.Error -> {
                    error = result.message
                    emit(emptyList())
                }
                is Resource.Loading -> loading = true
            }
        } else {
            emit(emptyList())
        }
    }


    fun addCity(city: String) {
        viewModelScope.launch {
            addUserCityUseCase(city)
            getCitiesWeatherFlow()
        }
    }

}