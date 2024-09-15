package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.API.Constant
import com.example.weatherapp.API.RetrofitInstance
import com.example.weatherapp.API.WeatherApi
import kotlinx.coroutines.launch


class WeatherViewModel:ViewModel() {

    val weatherApi:WeatherApi = RetrofitInstance.weatherApi

    fun getData(city:String){
        viewModelScope.launch {
            val response = weatherApi.getWeather(Constant.apiKey, city)
            if (response.isSuccessful){
                Log.i("location current", response.body().toString())
            }
        }
    }

}