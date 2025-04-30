package com.example.weatherapp.API

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val baseUrl = "https://weather-backend-42g7.onrender.com/"

    //https://orw80.wiremockapi.cloud/

    // Configure OkHttpClient with timeout settings
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(90, TimeUnit.SECONDS)    // wait up to 60s for connection
        .readTimeout(90, TimeUnit.SECONDS)       // wait up to 60s for the response
        .writeTimeout(90, TimeUnit.SECONDS)      // wait up to 60s to send data
        .build()

    // Build Retrofit instance
    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient) // attach custom client with timeouts
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Public Weather API instance
    val weatherApi: WeatherApi = getInstance().create(WeatherApi::class.java)
}
