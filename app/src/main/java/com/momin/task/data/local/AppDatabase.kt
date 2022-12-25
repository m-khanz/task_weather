package com.momin.task.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.momin.task.data.local.dao.CityDao
import com.momin.task.data.local.entity.DataEntity

@Database(version = 1, entities = [DataEntity::class],)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCityDAO(): CityDao
}