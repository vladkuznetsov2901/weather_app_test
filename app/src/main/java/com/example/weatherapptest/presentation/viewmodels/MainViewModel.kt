package com.example.weatherapptest.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.domain.models.Resource
import com.example.weatherapptest.domain.usecases.GetCurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel() {

    private val _citiesWeather = MutableStateFlow<List<CurrentWeather>>(emptyList())
    val citiesWeather: StateFlow<List<CurrentWeather>> = _citiesWeather

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val baseCities = listOf("Moscow", "New York")

    fun getBaseCities() {
        viewModelScope.launch {
            _loading.value = true
            val citiesInfo = mutableListOf<CurrentWeather>()

            for (city in baseCities) {
                when (val result = getCurrentWeatherUseCase(city)) {
                    is Resource.Success -> {
                        result.data.let { citiesInfo.add(it) }
                    }
                    is Resource.Error -> {
                        _error.value = result.message ?: "Unknown error"
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

}