package com.example.myweather

import android.app.Application
import com.example.myweather.api.MyWeatherServiceInterface
import com.example.myweather.api.RetrofitHelper
import com.example.myweather.respository.WeatherRepository

class WeatherApplication : Application() {

    lateinit var weatherRepository : WeatherRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val weatherService = RetrofitHelper.getRetrofitInstance().create(MyWeatherServiceInterface::class.java)
        weatherRepository = WeatherRepository(weatherService, applicationContext)
    }
}