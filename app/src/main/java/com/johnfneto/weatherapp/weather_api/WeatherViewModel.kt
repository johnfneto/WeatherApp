package com.johnfneto.weatherapp.weather_api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnfneto.weatherapp.BuildConfig
import com.johnfneto.weatherapp.models.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class WeatherViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val weatherApi: WeatherAPI = Service().createService(WeatherAPI::class.java)

    fun getWeather(city: String) {
        viewModelScope.launch {  // Dispatchers.Main
            val response: Response<WeatherModel> = getWeatherFromServer(city)
            if (response.isSuccessful) {
                Log.d(TAG, "response = $response")
                WeatherRepository.weatherData.postValue(response.body())
            } else {
                WeatherRepository.errorStatus.postValue(true)
            }
        }
    }

    private suspend fun getWeatherFromServer(city: String) = withContext(Dispatchers.IO) {
        weatherApi.getWeatherByCityAsync(city, "metric", BuildConfig.API_KEY)
    }

    fun getWeatherByZipCode(zipCode: String) {
        viewModelScope.launch {  // Dispatchers.Main
            val response: Response<WeatherModel> = getWeatherByZipCodeFromServer(zipCode)
            if (response.isSuccessful) {
                Log.d(TAG, "response = $response")
                WeatherRepository.weatherData.postValue(response.body())
            } else {
                WeatherRepository.errorStatus.postValue(true)
            }
        }
    }

    private suspend fun getWeatherByZipCodeFromServer(zipCode: String) = withContext(Dispatchers.IO) {
        weatherApi.getWeatherByZipCodeAsync(zipCode, "metric", BuildConfig.API_KEY)
    }

    fun getWeatherByCoord(lat: String, lon: String) {
        viewModelScope.launch { // Dispatchers.Main
            val response: Response<WeatherModel> = getWeatherByCoordFromServer(lat, lon)
            if (response.isSuccessful) {
                Log.d(TAG, "response = $response")
                WeatherRepository.weatherData.postValue(response.body())
            } else {
                WeatherRepository.errorStatus.postValue(true)
            }
        }
    }

    private suspend fun getWeatherByCoordFromServer(lat: String, lon: String) = withContext(Dispatchers.IO) {
        weatherApi.getWeatherByCoordAsync(lat, lon, "metric", BuildConfig.API_KEY)
    }
}