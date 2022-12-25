package com.task.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.momin.task.data.local.AppDatabase
import com.momin.task.data.local.entity.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CitiesDAOTest {

    private lateinit var mDatabase: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        //this creates a database in memory
        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    // test for the adding city data to the db and if that record is present in db, then test case is passed
    @Test
    @Throws(InterruptedException::class)
    fun add_cities_to_db() = runBlocking {
        val dataList = listOf(
            DataEntity(
                id = 1,
                CityEntity(
                    id = 1,
                    name = "City Name 1",
                    findname = "Search Name 1",
                    country = "Country 1",
                    coord = CoordEntity(0.9999,3.5555),
                    zoom = 9.0
                ),
                time = 10,
                cityName = "City Name 1",
                main = MainEntity(25.0,19.9, 10.3,15.4,25.8),
                weather = listOf(WeatherEntity(1,"","",""))
            ),
            DataEntity(
                id = 1,
                CityEntity(
                    id = 1,
                    name = "City Name 2",
                    findname = "Search Name 2",
                    country = "Country 2",
                    coord = CoordEntity(0.9999,3.5555),
                    zoom = 9.0
                ),
                time = 10,
                cityName = "City Name 2",
                main = MainEntity(25.0,19.9, 10.3,15.4,25.8),
                weather = listOf(WeatherEntity(1,"","",""))
            )
        )

        mDatabase.getCityDAO().addAllCities(dataList)
        var dbCities = mDatabase.getCityDAO().getAllCities()
        assertThat(dbCities, equalTo(dataList))
    }

    @Test
    @Throws(InterruptedException::class)
    fun show_cities_details_by_id() = runBlocking {
        val dataList = listOf(
            DataEntity(
                id = 1,
                CityEntity(
                    id = 1,
                    name = "City Name 2",
                    findname = "Search Name 2",
                    country = "Country 2",
                    coord = CoordEntity(0.9999,3.5555),
                    zoom = 9.0
                ),
                time = 10,
                cityName = "City Name 1",
                main = MainEntity(25.0,19.9, 10.3,15.4,25.8),
                weather = listOf(WeatherEntity(1,"","",""))
            )
        )

        mDatabase.getCityDAO().addAllCities(dataList)
        var dbCity = mDatabase.getCityDAO().getCityById(1).first()
        assertThat(dbCity, equalTo(dataList[0]))
    }

    @After
    fun tearDown() {
        mDatabase.close()
    }
}