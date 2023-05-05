package com.htn.weatherprediction

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.htn.weatherprediction.databinding.ActivityMainBinding
import com.htn.weatherprediction.model.Data
import com.htn.weatherprediction.model.Weather
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val myApi = retrofit.create(Service::class.java)

        getWeather(myApi)
        binding.refreshButton.setOnClickListener { getWeather(myApi) }
    }

    private fun getWeather(myApi: Service) {
        val call = myApi.getPrediction()
        call.enqueue(object : Callback<Data<List<Weather>>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<Data<List<Weather>>>,
                response: Response<Data<List<Weather>>>
            ) {
                if (response.isSuccessful) {
                    val list = response.body()?.data
                    val weather = if (list != null && list.isNotEmpty()) list[0] else null
                    if (weather != null) {
                        changeIcon(weather.guess)
                        binding.dateTxt.text = "Data got at: ${weather.date}"
                        binding.temperature.text = weather.temp.toString()
                        binding.predict.text = weather.guess
                        binding.pressureTxt.text = weather.pressure.toString()
                        binding.windSpeedTxt.text = weather.windSpeed.toString()
                        binding.directionTxt.text = weather.direction.toString()
                        binding.humidityTxt.text = weather.humid.toString()
                    }
                } else {
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<Data<List<Weather>>>, t: Throwable) {
                // Handle the error
            }
        })
    }

    private fun changeIcon(predict: String) {
        when (predict) {
            "Partly Cloudy" -> binding.image.setImageResource(R.drawable.ic_cloudy)
            "Mostly Cloudy" -> binding.image.setImageResource(R.drawable.ic_very_cloudy)
            "Overcast" -> binding.image.setImageResource(R.drawable.typeic_cloudy)
            "Clear" -> binding.image.setImageResource(R.drawable.typeic_sunny)
            "Foggy" -> binding.image.setImageResource(R.drawable.typeic_very_cloudy)
            "Breezy and Overcast" -> binding.image.setImageResource(R.drawable.typeic_very_cloudy)
            "Breezy and Mostly Cloudy" -> binding.image.setImageResource(R.drawable.typeic_very_cloudy)
            "Breezy and Partly Cloudy" -> binding.image.setImageResource(R.drawable.typeic_very_cloudy)
            "Dry and Partly Cloudy" -> binding.image.setImageResource(R.drawable.typeic_very_cloudy)
            "Windy and Partly Cloudy" -> binding.image.setImageResource(R.drawable.typeic_very_cloudy)
            "Light Rain" -> binding.image.setImageResource(R.drawable.ic_rainy)
            else -> binding.image.setImageResource(R.drawable.typeic_sunny)
        }
    }
}