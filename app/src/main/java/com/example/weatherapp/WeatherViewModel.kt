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
        getData("madurai")
    }

    fun getData(city:String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {

                val response = weatherApi.getWeather(ConstantKey.apiKey, city, ConstantDays.days)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load the data")
                }
            }
            catch (e:Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load the data")
            }

        }
    }

}