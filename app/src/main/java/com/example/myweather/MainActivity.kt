package com.example.myweather

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myweather.api.MyWeatherServiceInterface
import com.example.myweather.api.RetrofitHelper
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.respository.WeatherRepository
import com.example.myweather.viewmodel.MainViewModel
import com.example.myweather.viewmodel.MainViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val weatherService =
            RetrofitHelper.getRetrofitInstance().create(MyWeatherServiceInterface::class.java)

        val repository = WeatherRepository(weatherService)

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.weather.observe(this) {
            Log.d("testmvvm", it.main.temp.toString())
            binding.mainTemperature.text = it.main.temp.toString().plus(" ").plus("\u2103")
            binding.max.text = it.main.temp_max.toString().plus(" ").plus("\u2103")
            binding.min.text = it.main.temp_min.toString().plus(" ").plus("\u2103")
            binding.locName.text = it.name.plus(",").plus(it.sys.country)
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/".plus(it.weather[0].icon).plus("@4x.png"))
                .into(binding.icon)
            binding.desc.text = it.weather[0].description
            binding.humidity.text = it.main.humidity.toString().plus("%")
            binding.wind.text = it.wind.speed.toString().plus("km/h")
        }

        binding.mainViewModel = mainViewModel

    }
}