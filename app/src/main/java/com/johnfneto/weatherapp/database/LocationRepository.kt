package com.johnfneto.weatherapp.database

import androidx.lifecycle.LiveData

class LocationRepository(private val locationDao: LocationDao) {

    val locationsList: LiveData<List<WeatherLocation>> = locationDao.getLocationsList()

    suspend fun insert(location: WeatherLocation) {
        locationDao.saveLocation(location)
    }

    suspend fun delete(uid: Int) {
        locationDao.deleteLocation(uid)
    }
}