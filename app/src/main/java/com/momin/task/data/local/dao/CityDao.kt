package com.momin.task.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.momin.task.data.local.entity.DataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllCities(articleEntity: List<DataEntity>): List<Long>

    // here we are using livedata, so no need for suspend func...
    @Query("SELECT * FROM data")
    fun getAllCities(): PagingSource<Int, DataEntity>

    @Query("SELECT COUNT(*) from data")
    fun citiesCount(): Int

    //query for search..
    @Query("SELECT * FROM data WHERE cityName LIKE '%' || :searchQuery || '%'")
    fun getFilteredCities(searchQuery: String): PagingSource<Int, DataEntity>

    //get city data from table with unique id...
    @Query("SELECT * FROM data WHERE ID = :id")
    fun getCityById(id: Long): Flow<DataEntity>
}