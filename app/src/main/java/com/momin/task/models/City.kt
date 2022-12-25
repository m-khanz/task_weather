package com.momin.task.models

import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("findname") val findname: String,
    @SerializedName("country") val country: String,
    @SerializedName("coord") val coord: Coordinates,
    @SerializedName("zoom") val zoom: Double
)