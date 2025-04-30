package com.example.weatherapp.API

data class WeatherModel(
    val airCondition: List<AirCondition>,
    val city: String,
    val dayForecast: List<DayForecast>,
    val img: String,
    val temp: String,
    val todayForecast: List<TodayForecast>,
    val weather: String
)