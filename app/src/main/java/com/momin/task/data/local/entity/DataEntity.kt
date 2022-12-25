package com.momin.task.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class DataEntity (
	@PrimaryKey(autoGenerate = true) var id: Int = 0,
	val city : CityEntity,
	val time : Long,
	val cityName: String,
	val main : MainEntity,
	val weather : List<WeatherEntity>
)