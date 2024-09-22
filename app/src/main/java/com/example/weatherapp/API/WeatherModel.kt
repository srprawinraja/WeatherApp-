package com.example.weatherapp.API

data class WeatherModel(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)