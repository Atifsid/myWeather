package com.example.myweather

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myweather.databinding.ActivityMainBinding
import com.example.myweather.respository.Response
import com.example.myweather.viewmodel.MainViewModel
import com.example.myweather.viewmodel.MainViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.sembozdemir.permissionskt.askPermissions


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.askPermissions(Manifest.permission.INTERNET) {
            onGranted { Log.d("Permissions", "Internet permissions granted") }
            onDenied { permissions ->
                permissions.forEach {
                    when (it) {
                        Manifest.permission.INTERNET -> Toast.makeText(
                            this@MainActivity,
                            "Allow internet permissions from settings",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            onShowRationale { request ->
                Snackbar.make(
                    binding.coordinatorLayout,
                    "You should grant Internet permission",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Retry") { request.retry() }
                    .show()
            }
            onNeverAskAgain {
                finish()
            }
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.info.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.title))
                .setMessage(resources.getString(R.string.supporting_text))
                .setPositiveButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    // Respond to neutral button press
                    dialog.dismiss()
                }
                .show()
        }

        val repository = (application as WeatherApplication).weatherRepository

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.loactionStatus.observe(this, Observer {
            if(mainViewModel.loactionStatus.value == false) {
                Toast.makeText(this,"Error 404: No Location found",Toast.LENGTH_SHORT).show()
            }
        })

        mainViewModel.weather.observe(this) { fetchedData ->
            when(fetchedData){
                is Response.Loading ->{ binding.progressBarCyclic.visibility = View.VISIBLE }
                is Response.Error ->{
                    fetchedData.errorMessage
                    Toast.makeText(this,fetchedData.errorMessage,Toast.LENGTH_SHORT).show()
                }
                is Response.Success ->{
                    fetchedData.data?.let {
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

                }
            }
        }

        binding.mainViewModel = mainViewModel

    }

}