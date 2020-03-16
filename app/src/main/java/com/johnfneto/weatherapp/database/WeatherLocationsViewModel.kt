package com.johnfneto.weatherapp.database

import android.app.Application
import androidx.lifecycle.*
import com.johnfneto.weatherapp.models.WeatherModel
import kotlinx.coroutines.launch

class WeatherLocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocationRepository
    val locationsList: LiveData<List<WeatherModel>>
    val dropdownLocationsList: LiveData<List<String>>

    init {
        val locationsDao = AppDataBase.getDatabase(application, viewModelScope).locationDao()
        repository = LocationRepository(locationsDao)
        locationsList = repository.locationsList
        dropdownLocationsList = Transformations.map(locationsList, ::getDropdownList)
    }

    private fun getDropdownList(locationsList: List<WeatherModel>): List<String> {
        val locationsMap = locationsList.map { location ->
            location.name + ", " + location.sys.country
        }.toMutableList()
        locationsMap.add(0, "Current location")
        return locationsMap
    }

    fun saveLocation(location: WeatherModel) {
        insert(location)
    }

    private fun insert(location: WeatherModel) = viewModelScope.launch {
        repository.insert(location)
    }

    fun deleteLocation(uid: Int) = viewModelScope.launch {
        repository.delete(uid)
    }
}
