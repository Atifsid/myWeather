package com.example.myweather.respository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweather.api.MyWeatherServiceInterface
import com.example.myweather.dataModels.MyWeather

//Repository is used to manage our data. It needs access to retrofit service to do so.
class WeatherRepository(private val myWeatherService: MyWeatherServiceInterface) {

    private val weatherLiveData = MutableLiveData<MyWeather>()
    //Will be called in MainViewModel.
    val weather: LiveData<MyWeather>
        get() = weatherLiveData

    suspend fun getWeather(location: String, units: String) {
        val result = myWeatherService.getWeather(location,units)
        if(result?.body() != null){
            weatherLiveData.postValue(result.body())
        }
    }
}