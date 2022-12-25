package com.momin.task.models

import com.google.gson.annotations.SerializedName

data class Data (
    @SerializedName("city") val city : City,
    @SerializedName("weather") val weather : List<Weather>,
    @SerializedName("time") val time : Long,
    @SerializedName("main") val main : Main
)