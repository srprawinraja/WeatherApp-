package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.API.ConstantDays
import com.example.weatherapp.API.ConstantKey
import com.example.weatherapp.API.NetworkResponse
import com.example.weatherapp.API.RetrofitInstance
import com.example.weatherapp.API.WeatherApi
import com.example.weatherapp.API.WeatherModel
import kotlinx.coroutines.launch


class WeatherViewModel:ViewModel() {


    private val weatherApi:WeatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult
    init{
        Log.i("responsee", "init")
        getData("madurai")
    }

    fun getData(city:String){
        Log.i("responsee", "get data")
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                Log.i("responsee", "started")

                val response = weatherApi.getWeather(ConstantKey.apiKey, city, ConstantDays.days)

                if (response.isSuccessful) {
                    Log.i("responsee", response.code().toString())
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    Log.i("responsee", response.code().toString())
                    _weatherResult.value = NetworkResponse.Error("Failed to load the data")
                }
            }
            catch (e:Exception){
                Log.i("responsee", "error")
                _weatherResult.value = NetworkResponse.Error("Failed to load the data")
            }

        }
    }

}