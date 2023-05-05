package com.htn.weatherprediction.model

import com.google.gson.annotations.SerializedName

data class Weather(
    val date: String,
    val temp: Float,
    val pressure: Float,
    @SerializedName("wind_speed")
    val windSpeed: Float,
    val rain: Float,
    val direction: Float,
    val humid: Float,
    val guess: String,
)