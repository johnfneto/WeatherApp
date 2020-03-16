package com.johnfneto.weatherapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.johnfneto.weatherapp.database.AppDataBase
import com.johnfneto.weatherapp.database.LocationDao
import com.johnfneto.weatherapp.models.*
import com.johnfneto.weatherapp.utils.waitForValue
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@Suppress("BlockingMethodInNonBlockingContext")
@RunWith(AndroidJUnit4::class)
class LocationDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var locationDao: LocationDao
    private lateinit var db: AppDataBase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        locationDao = db.locationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetLocation() = runBlocking {
        val location = WeatherModel(
            null,
            Date().time,
            Coord(0.0,0.0),
            listOf(),
            "",
            Main(0.0, 0.0, 0.0, 0.0, 0, 0),
            0,
            Wind(0.0, 0),
            Clouds(0),
            0,
            Sys(0, "", 0, 0),
            0,
            0,
            "NoWhereCity",
            0
        )
        locationDao.saveLocation(location)
        val locationsList = locationDao.getLocationsList().waitForValue()
        assertEquals(locationsList[0].name, location.name)
    }

    @Test
    @Throws(Exception::class)
    fun getAllLocations() = runBlocking {
        val myLocation = WeatherModel(
            null,
            Date().time,
            Coord(0.0,0.0),
            listOf(),
            "",
            Main(0.0, 0.0, 0.0, 0.0, 0, 0),
            0,
            Wind(0.0, 0),
            Clouds(0),
            0,
            Sys(0, "", 0, 0),
            0,
            0,
            "myCity",
            0
        )
        locationDao.saveLocation(myLocation)
        val yourLocation = WeatherModel(
            null,
            Date().time,
            Coord(0.0,0.0),
            listOf(),
            "",
            Main(0.0, 0.0, 0.0, 0.0, 0, 0),
            0,
            Wind(0.0, 0),
            Clouds(0),
            0,
            Sys(0, "", 0, 0),
            0,
            0,
            "yourCity",
            0
        )
        locationDao.saveLocation(yourLocation)
        val locationsList = locationDao.getLocationsList().waitForValue()
        assertEquals(locationsList[1].name, myLocation.name)
        assertEquals(locationsList[0].name, yourLocation.name)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllLocations() = runBlocking {
        val myLocation = WeatherModel(
            null,
            Date().time,
            Coord(0.0,0.0),
            listOf(),
            "",
            Main(0.0, 0.0, 0.0, 0.0, 0, 0),
            0,
            Wind(0.0, 0),
            Clouds(0),
            0,
            Sys(0, "", 0, 0),
            0,
            0,
            "myCity",
            0
        )
        locationDao.saveLocation(myLocation)
        val yourLocation = WeatherModel(
            null,
            Date().time,
            Coord(0.0,0.0),
            listOf(),
            "",
            Main(0.0, 0.0, 0.0, 0.0, 0, 0),
            0,
            Wind(0.0, 0),
            Clouds(0),
            0,
            Sys(0, "", 0, 0),
            0,
            0,
            "yourCity",
            0
        )
        locationDao.saveLocation(yourLocation)
        locationDao.deleteAll()
        val locationsList = locationDao.getLocationsList().waitForValue()
        Assert.assertTrue(locationsList.isEmpty())
    }
}