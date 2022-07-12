package com.example.myweather.viewmodel

import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.dataModels.MyWeather
import com.example.myweather.respository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Parameterized ViewModel so it requires a factory.
class MainViewModel(private val repository: WeatherRepository) : ViewModel() {
    var location: String? = null

    fun setOnSearchbtnClick() {
        viewModelScope.launch(Dispatchers.IO) {
            //MainViewModel calls getWeather to fetch weather info
            repository.getWeather(location!!.trim(), "metric")
        }
    }

    val weather: LiveData<MyWeather>
        get() = repository.weather


}