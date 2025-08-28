package com.example.weatherapptest.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapptest.R
import com.example.weatherapptest.domain.models.CurrentWeather
import com.example.weatherapptest.presentation.viewmodels.MainViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {


    var newCity by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            viewModel.searchCities(query)
                            showSuggestions = query.isNotBlank()
                        },
                        placeholder = { Text(stringResource(R.string.enter_the_city)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (showSuggestions) {
                        LazyColumn {
                            items(viewModel.suggestions) { city ->
                                Text(
                                    text = "${city.name}, ${city.country}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            query = city.name
                                            showSuggestions = false
                                            newCity = query
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        if (newCity.isNotBlank()) {
                            viewModel.addCity(newCity)
                            query = ""
                            newCity = ""
                        }
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(stringResource(R.string.plus))
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
            if (viewModel.loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(viewModel.citiesWeather) { city ->
                        CityItem(city) {navController.navigate("cityDetails/${city.cityName}")}
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun CityItem(city: CurrentWeather, onCityClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onCityClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = city.cityName, fontSize = 18.sp)
        Text(text = "${city.temperature.roundToInt()}°C", fontSize = 18.sp)
    }
}