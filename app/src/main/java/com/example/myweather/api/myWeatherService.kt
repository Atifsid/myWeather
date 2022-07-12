package com.example.myweather.api

import com.example.myweather.BuildConfig
import com.example.myweather.dataModels.MyWeather
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val myApiKey: String = BuildConfig.API_KEY

interface MyWeatherServiceInterface {
    @GET("data/2.5/weather?appid=$myApiKey")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String
    ): Response<MyWeather>
}