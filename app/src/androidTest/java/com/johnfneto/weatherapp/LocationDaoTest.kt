package com.johnfneto.weatherapp

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.johnfneto.weatherapp.database.AppDataBase
import com.johnfneto.weatherapp.database.LocationDao
import com.johnfneto.weatherapp.models.WeatherLocation
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
        val location = WeatherLocation(
            null,
            "NoWhereCity",
            Date().time
        )
        locationDao.saveLocation(location)
        val locationsList = locationDao.getLocationsList().waitForValue()
        assertEquals(locationsList[0].city, location.city)
    }

    @Test
    @Throws(Exception::class)
    fun getAllLocations() = runBlocking {
        val myLocation = WeatherLocation(
            null,
            "myCity",
            Date().time
        )
        locationDao.saveLocation(myLocation)
        val yourLocation = WeatherLocation(
            null,
            "yourCity",
            Date().time
        )
        locationDao.saveLocation(yourLocation)
        val locationsList = locationDao.getLocationsList().waitForValue()
        assertEquals(locationsList[0].city, myLocation.city)
        assertEquals(locationsList[1].city, yourLocation.city)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllLocations() = runBlocking {
        val myLocation = WeatherLocation(
            null,
            "myCity",
            Date().time
        )
        locationDao.saveLocation(myLocation)
        val yourLocation = WeatherLocation(
            null,
            "yourCity",
            Date().time
        )
        locationDao.saveLocation(yourLocation)
        locationDao.deleteAll()
        val locationsList = locationDao.getLocationsList().waitForValue()
        Assert.assertTrue(locationsList.isEmpty())
    }
}