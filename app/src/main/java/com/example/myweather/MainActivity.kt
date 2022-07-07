package com.example.myweather

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myweather.data.MyWeatherDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.scBtn)
        btn.setOnClickListener { getMyWeather() }
    }

    private fun getMyWeather() {
        val scText = findViewById<EditText>(R.id.scText)
        var myCitySearch = scText.text.toString()
        val myCityWeather = WeatherService.weatherInstance.getWeather(myCitySearch,"metric")
        myCityWeather.enqueue(object: Callback<MyWeatherDataModel>{
            override fun onResponse(
                call: Call<MyWeatherDataModel>,
                response: Response<MyWeatherDataModel>
            ) {
                val myCityWeatherFinal = response.body()
                if(myCityWeatherFinal != null){
                    Log.d("testres","passssss")
                    var myCityName: TextView = findViewById<TextView>(R.id.cityName)
                    var myCityTemp: TextView = findViewById<TextView>(R.id.mainTemperature)
                    var myCityMax: TextView = findViewById<TextView>(R.id.max)
                    var myCityMin: TextView = findViewById<TextView>(R.id.min)
                    var myCityPressure: TextView = findViewById<TextView>(R.id.pressure)
                    var myCityWind: TextView = findViewById<TextView>(R.id.wind)

                    myCityName.text = myCityWeatherFinal.name
                    myCityTemp.text = myCityWeatherFinal.main.temp.toString().plus(" ").plus("\u2103")

                    myCityMax.text = myCityWeatherFinal.main.temp_max.toString().plus(" ").plus("\u2103")
                    myCityMin.text = myCityWeatherFinal.main.temp_min.toString().plus(" ").plus("\u2103")
                    myCityPressure.text = myCityWeatherFinal.main.pressure.toString().plus(" ").plus("mbar")
                    myCityWind.text = myCityWeatherFinal.wind.speed.toString().plus(" ").plus("km/h")
                }

            }

            override fun onFailure(call: Call<MyWeatherDataModel>, t: Throwable) {
                Log.d("testres","fail")
            }
        })
    }


}