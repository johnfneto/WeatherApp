package com.johnfneto.weatherapp.weather_api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnfneto.weatherapp.BuildConfig
import com.johnfneto.weatherapp.database.LocationRepository
import com.johnfneto.weatherapp.models.WeatherModel
import com.johnfneto.weatherapp.utils.OpenForTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException

@OpenForTesting
class WeatherViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val repository: WeatherRepository
    final var weatherData = MutableLiveData<WeatherModel>()
    final var errorStatus = MutableLiveData<String>()
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
            //weatherApi.getWeatherByCityAsync(city, BuildConfig.API_KEY)
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
                //weatherApi.getWeatherByZipCodeAsync(zipCode, BuildConfig.API_KEY)
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
                //weatherApi.getWeatherByCoordAsync(lat, lon, "metric", BuildConfig.API_KEY)
            } catch (e: IOException) {
                buildErrorResponse(e.message)
            }
        }

    private fun buildErrorResponse(message: String?) = Response.error<WeatherModel>(
        400,
        "{\"key\":[$message]}"
            .toResponseBody("application/json".toMediaTypeOrNull())
    )
}