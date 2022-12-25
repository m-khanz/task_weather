package com.momin.task.data.local.entity

data class CityEntity(
    var id: Int = 0,
    val name: String,
    val findname: String,
    val country: String,
    val coord: CoordEntity,
    val zoom: Double
)