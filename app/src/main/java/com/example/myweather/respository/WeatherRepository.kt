package com.example.myweather.respository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweather.api.MyWeatherServiceInterface
import com.example.myweather.dataModels.MyWeather
import com.example.myweather.utils.NetworkUtils
import com.example.myweather.viewmodel.MainViewModel
import java.io.IOException

//Repository is used to manage our data. It needs access to retrofit service to do so.
class WeatherRepository(
    private val myWeatherService: MyWeatherServiceInterface,
    private val applicationContext: Context
) {

    private val weatherLiveData = MutableLiveData<Response<MyWeather>>()
    //Will be called in MainViewModel.
    val weather: LiveData<Response<MyWeather>>
        get() = weatherLiveData

    suspend fun getWeather(location: String, units: String) {
        if(NetworkUtils.isInternetAvailable(applicationContext)){
                val result = myWeatherService.getWeather(location,units)
                if(result?.body() != null){
                    weatherLiveData.postValue(Response.Success(result.body()))
                }
                else{
                    weatherLiveData.postValue(Response.Error("Error 404: Location not found"))
                }
        }
        else{
            //handle no internet
            weatherLiveData.postValue(Response.Error("No Internet"))
        }

    }
}
