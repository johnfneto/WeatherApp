package com.johnfneto.weatherapp.weather_api

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnfneto.weatherapp.BuildConfig
import com.johnfneto.weatherapp.models.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException

class WeatherViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val weatherApi: WeatherAPI = Service().createService(WeatherAPI::class.java)

    fun getWeather(city: String) {
        viewModelScope.launch {
            // Dispatchers.Main
            val response: Response<WeatherModel> =
                getWeatherFromServer(city) as Response<WeatherModel>
            if (response.isSuccessful) {
                WeatherRepository.weatherData.postValue(response.body())
            } else {
                WeatherRepository.errorStatus.postValue(response.message())
            }
        }
    }

    private suspend fun getWeatherFromServer(city: String) = withContext(Dispatchers.IO) {
        try {
            weatherApi.getWeatherByCityAsync(city, "metric", BuildConfig.API_KEY)
        } catch (e: IOException) {
            Response.error<Response<WeatherModel>>(
                400,
                "{\"key\":[${e.message}]}"
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
        }
    }

    fun getWeatherByZipCode(zipCode: String) {
        viewModelScope.launch {
            // Dispatchers.Main
            val response: Response<WeatherModel> = getWeatherByZipCodeFromServer(zipCode)  as Response<WeatherModel>
            if (response.isSuccessful) {
                Log.d(TAG, "response = $response")
                WeatherRepository.weatherData.postValue(response.body())
            } else {
                WeatherRepository.errorStatus.postValue(response.message())
            }
        }
    }

    private suspend fun getWeatherByZipCodeFromServer(zipCode: String) =
        withContext(Dispatchers.IO) {
            try {
                weatherApi.getWeatherByZipCodeAsync(zipCode, "metric", BuildConfig.API_KEY)
            } catch (e: IOException) {
                Response.error<Response<WeatherModel>>(
                    400,
                    "{\"key\":[${e.message}]}"
                        .toResponseBody("application/json".toMediaTypeOrNull())
                )
            }
        }

    fun getWeatherByCoord(lat: String, lon: String) {
        viewModelScope.launch {
            // Dispatchers.Main
            val response: Response<WeatherModel> = getWeatherByCoordFromServer(lat, lon)  as Response<WeatherModel>
            if (response.isSuccessful) {
                Log.d(TAG, "response = $response")
                WeatherRepository.weatherData.postValue(response.body())
            } else {
                WeatherRepository.errorStatus.postValue(response.message())
            }
        }
    }

    private suspend fun getWeatherByCoordFromServer(lat: String, lon: String) =
        withContext(Dispatchers.IO) {
            try {
                weatherApi.getWeatherByCoordAsync(lat, lon, "metric", BuildConfig.API_KEY)
            } catch (e: IOException) {
                Response.error<Response<WeatherModel>>(
                    400,
                    "{\"key\":[${e.message}]}"
                        .toResponseBody("application/json".toMediaTypeOrNull())
                )
            }
        }
}