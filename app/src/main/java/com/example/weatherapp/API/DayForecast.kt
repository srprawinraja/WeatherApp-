package com.example.weatherapp.API

data class DayForecast(
    val day: String,
    val img: String,
    val tempMax: String,
    val tempMin: String,
    val weather: String
)