package com.htn.weatherprediction

import com.htn.weatherprediction.model.Data
import com.htn.weatherprediction.model.Weather
import retrofit2.Call
import retrofit2.http.GET

interface Service {

    @GET("send_data/")
    fun getPrediction(): Call<Data<List<Weather>>>
}