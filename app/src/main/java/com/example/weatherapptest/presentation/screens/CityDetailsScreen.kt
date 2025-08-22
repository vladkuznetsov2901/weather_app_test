package com.example.weatherapptest.presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapptest.R
import com.example.weatherapptest.domain.models.DailyForecast
import com.example.weatherapptest.presentation.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun CityDetailsScreen(
    cityName: String,
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavController
) {
    var daysToShow by remember { mutableIntStateOf(7) }
    val city by viewModel.cityWeatherByName.collectAsState(initial = null)
    val forecast by viewModel.forecast.collectAsState(initial = null)

    val daysNumbers = listOf(3, 7)

    LaunchedEffect(Unit) {
        viewModel.getCityWeatherByName(cityName)
    }

    LaunchedEffect(city) {
        city?.let { weather ->
            viewModel.getForecastByCity(cityName, weather.coord.lat, weather.coord.lon)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CityHeader(
                cityName = cityName,
                temperature = city?.temperature?.roundToInt(),
                description = city?.description
            )

            Spacer(modifier = Modifier.height(24.dp))

            DaysSelector(daysNumbers, daysToShow) { daysToShow = it }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(forecast?.daily?.take(daysToShow) ?: emptyList()) { day ->
                    DailyForecastItem(day)
                }
            }
        }

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(stringResource(R.string.back_text))
        }
    }

}


@Composable
fun CityHeader(cityName: String, temperature: Int?, description: String?) {
    Column {
        Text(text = cityName, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(16.dp))
        temperature?.let {
            Text(text = stringResource(R.string.temperature_text, it), fontSize = 20.sp)
        }
        description?.let {
            Text(text = stringResource(R.string.state_text, it), fontSize = 16.sp)
        }
    }
}

@Composable
fun DaysSelector(daysNumbers: List<Int>, selectedIndex: Int, onChange: (Int) -> Unit) {
    SingleChoiceSegmentedButtonRow {
        daysNumbers.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = daysNumbers.size),
                onClick = { onChange(label) },
                selected = label == selectedIndex,
            ) {
                when (label) {
                    3 -> Text(stringResource(R.string.count_days_text, label))
                    7 -> Text(stringResource(R.string.count_days_text2, label))
                }

            }
        }
    }
}

@Composable
fun DailyForecastItem(day: DailyForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0F7FA))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = SimpleDateFormat("dd MMM", Locale.getDefault()).format(day.date),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${day.tempMin.roundToInt()}°C / ${day.tempMax.roundToInt()}°C",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = day.description,
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("💧 ${day.humidity}%", style = MaterialTheme.typography.bodyMedium)
                Text("🌡 ${day.pressure} hPa", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "🌬 ${"%.1f".format(day.wind.speed)} ${day.wind.unit}, ${day.wind.deg}°",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}



