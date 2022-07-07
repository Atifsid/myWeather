package com.example.myweather

import com.example.myweather.data.MyWeatherDataModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org/"
const val myApiKey: String = BuildConfig.API_KEY

interface MyWeatherServiceInterface {
    @GET("data/2.5/weather?appid=$myApiKey")
    fun getWeather(@Query("q") city: String, @Query("units") units: String) : Call<MyWeatherDataModel>
}

object WeatherService{
    val weatherInstance: MyWeatherServiceInterface
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        weatherInstance = retrofit.create(MyWeatherServiceInterface::class.java)
    }
}