package com.johnfneto.weatherapp.weather_api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnfneto.weatherapp.models.WeatherModel
import com.johnfneto.weatherapp.utils.Utils.buildErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException


class WeatherViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    val repository: WeatherRepository
    var weatherData = MutableLiveData<WeatherModel>()
    var errorStatus = MutableLiveData<String>()
    private val weatherApi: WeatherAPI = Service().createService(WeatherAPI::class.java)

    init {
        repository = WeatherRepository(weatherApi)
        weatherData = repository.weatherData
        errorStatus = repository.errorStatus
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            // Dispatchers.Main
            val response: Response<WeatherModel> =
                getWeatherFromServer(city)
            if (response.isSuccessful) {
                repository.weatherData.postValue(response.body())
            } else {
                repository.errorStatus.postValue(response.message())
            }
        }
    }

    private suspend fun getWeatherFromServer(city: String): Response<WeatherModel> =
        withContext(Dispatchers.IO) {
        try {
            repository.getWeatherByCity(city)
        } catch (e: IOException) {
            buildErrorResponse(e.message)
        }
    }


    fun getWeatherByZipCode(zipCode: String) {
        viewModelScope.launch {
            // Dispatchers.Main
            val response: Response<WeatherModel> = getWeatherByZipCodeFromServer(zipCode)
            if (response.isSuccessful) {
                Log.d(TAG, "response = $response")
                repository.weatherData.postValue(response.body())
            } else {
                repository.errorStatus.postValue(response.message())
            }
        }
    }

    private suspend fun getWeatherByZipCodeFromServer(zipCode: String): Response<WeatherModel> =
        withContext(Dispatchers.IO) {
            try {
                repository.getWeatherByZipCode(zipCode)
            } catch (e: IOException) {
                buildErrorResponse(e.message)
            }
        }

    fun getWeatherByCoord(lat: String, lon: String) {
        viewModelScope.launch {
            // Dispatchers.Main
            val response: Response<WeatherModel> = getWeatherByCoordFromServer(lat, lon)
            if (response.isSuccessful) {
                Log.d(TAG, "response = $response")
                repository.weatherData.postValue(response.body())
            } else {
                repository.errorStatus.postValue(response.message())
            }
        }
    }

    private suspend fun getWeatherByCoordFromServer(lat: String, lon: String): Response<WeatherModel> =
        withContext(Dispatchers.IO) {
            try {
                repository.getWeatherByCoord(lat, lon)
            } catch (e: IOException) {
                buildErrorResponse(e.message)
            } as Response<WeatherModel>
        }
}