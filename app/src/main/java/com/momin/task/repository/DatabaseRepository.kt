package com.momin.task.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.momin.task.data.local.AppDatabase
import com.momin.task.utils.Transformer.convertDataListToDataEntityList
import com.momin.task.models.Data
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val appDatabase: AppDatabase) {

    /**
     * inserting all data to db
     */
    suspend fun insertAll(data: List<Data>): List<Long> {
        return appDatabase.getCityDAO()
            .addAllCities(convertDataListToDataEntityList(data))
    }

    /**
     * Getting all cities from local DB by using pager for pagination
     * NOTE - Since we are already using LIVE-DATA no need to use suspend function
     */
    fun getAllCities() =
        Pager(
            PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 100
            )
        ) {
            appDatabase.getCityDAO().getAllCities()
        }.liveData

    /**
     * Getting filtered list based on user input
     */
    fun getFilteredCities(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            )
        ) {
            appDatabase.getCityDAO().getFilteredCities(query)
        }.liveData

    /**
     * Check if DB has data or not
     */
    fun isDbEmpty(): Boolean {
        return appDatabase.getCityDAO().citiesCount() == 0
    }


}