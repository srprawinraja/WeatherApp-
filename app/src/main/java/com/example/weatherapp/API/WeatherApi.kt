package com.example.weatherapp.API

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface  WeatherApi{
    @GET("weather")
    suspend fun getWeather(
        @Query("city_name") city: String
    ):Response<WeatherModel>
}
