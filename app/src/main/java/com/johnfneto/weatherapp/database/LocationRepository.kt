package com.johnfneto.weatherapp.database

import androidx.lifecycle.LiveData
import com.johnfneto.weatherapp.models.WeatherModel

class LocationRepository(private val locationDao: LocationDao) {

    val locationsList: LiveData<List<WeatherModel>> = locationDao.getLocationsList()

    suspend fun insert(location: WeatherModel) {
        locationDao.saveLocation(location)
    }

    suspend fun delete(uid: Int) {
        locationDao.deleteLocation(uid)
    }
}