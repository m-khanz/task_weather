package com.momin.task.data.local.entity

import com.google.gson.annotations.SerializedName

data class WeatherEntity (

	@SerializedName("id") val id : Int,
	@SerializedName("main") val main : String,
	@SerializedName("description") val description : String,
	@SerializedName("icon") val icon : String
)