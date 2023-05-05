package com.htn.weatherprediction.model

import com.google.gson.annotations.SerializedName

data class Data<D>(
    val status: String,
    @SerializedName("status_code")
    val status_code: Int,
    val data: D
)

