package com.example.weatherapptest.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.presentation.viewmodels.MainViewModel

@Composable
fun WeatherScreen(
    onAddCity: ((String) -> Unit?)?,
    viewModel: MainViewModel = hiltViewModel()
) {
    val citiesWeather by viewModel.citiesWeather.collectAsState()
    var newCity by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getBaseCities()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newCity,
                onValueChange = { newCity = it },
                placeholder = { Text("Введите город") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (newCity.isNotBlank()) {
//                    onAddCity(newCity)
                    newCity = ""
                }
            }) {
                Text("Добавить")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(citiesWeather) { city ->
                CityItem(city = city)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun CityItem(city: CurrentWeather) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = city.cityName, fontSize = 18.sp)
        Text(text = "${city.temperature}°C", fontSize = 18.sp)
    }
}