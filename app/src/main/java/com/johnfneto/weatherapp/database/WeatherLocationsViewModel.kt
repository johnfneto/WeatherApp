package com.johnfneto.weatherapp.database

import android.app.Application
import androidx.lifecycle.*
import com.johnfneto.weatherapp.models.WeatherLocation
import kotlinx.coroutines.launch
import java.util.*

class WeatherLocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocationRepository
    val locationsList: LiveData<List<WeatherLocation>>
    val dropdownLocationsList: LiveData<List<String>>

    init {
        val locationsDao = AppDataBase.getDatabase(application, viewModelScope).locationDao()
        repository = LocationRepository(locationsDao)
        locationsList = repository.locationsList
        dropdownLocationsList = Transformations.map(locationsList, ::getDropdownList)
    }

    private fun getDropdownList(locationsList: List<WeatherLocation>): List<String> {
        val locationsMap = locationsList.map { location ->
            location.city
        }.toMutableList()
        locationsMap.add(0, "Current location")
        return locationsMap
    }

    fun saveLocation(city: String) {
        val location = WeatherLocation(
            null,
            city,
            Date().time
        )
        insert(location)
    }

    private fun insert(location: WeatherLocation) = viewModelScope.launch {
        repository.insert(location)
    }

    fun deleteLocation(uid: Int) = viewModelScope.launch {
        repository.delete(uid)
    }
}
