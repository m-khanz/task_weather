package com.momin.task.utils

import com.momin.task.data.local.entity.*
import com.momin.task.models.*

/**
 * This is a transformer class
 * We cannot just use our model classes for inserting or getting data from db
 * There might be a time when variables might be added or removed in model classes
 * So it is always a better practice to have transformer classes
 * */
object Transformer {

    fun convertDataListToDataEntityList(data: List<Data>): List<DataEntity> {
        val list = ArrayList<DataEntity>()
        for (data in data) {
            data?.let {
                list.add(
                    DataEntity(
                        id = data.city.id,
                        city = convertCityModelToCityEntity(data.city),
                        time = data.time,
                        cityName = data.city.name,
                        main = convertMainModelToMainEntity(data.main),
                        weather = convertWeatherModelToWeatherEntity(data.weather)
                    )
                )
            }

        }
        return list
    }

    private fun convertCityModelToCityEntity(city: City): CityEntity {
        return CityEntity(
            city.id, city.name, city.findname,
            city.country,
            convertCoordModelToCoordEntity(city.coord),
            city.zoom
        )
    }

    private fun convertCoordModelToCoordEntity(coord: Coordinates): CoordEntity {
        return CoordEntity(coord.lon, coord.lat)
    }

    private fun convertMainModelToMainEntity(main: Main): MainEntity {
        return MainEntity(
            temp = main.temp, pressure = main.pressure,
            humidity = main.humidity, temp_max = main.temp_max,
            temp_min = main.temp_min
        )
    }

    private fun convertWeatherModelToWeatherEntity(weather: List<Weather>): List<WeatherEntity> {
        val list = ArrayList<WeatherEntity>()
        weather.mapTo(list) {
            WeatherEntity(
                it.id, it.main,
                it.description,
                it.icon,
            )
        }
        return list
    }
}