package com.johnfneto.weatherapp.weather_api

import androidx.lifecycle.MutableLiveData
import com.johnfneto.weatherapp.BuildConfig
import com.johnfneto.weatherapp.models.WeatherModel
import retrofit2.Response

class WeatherRepository(private val weatherApi: WeatherAPI) {

    var weatherData = MutableLiveData<WeatherModel>()

    var errorStatus: MutableLiveData<String> = MutableLiveData()

    suspend fun getWeatherByCity(city: String): Response<WeatherModel> =
        weatherApi.getWeatherByCityAsync(city,  BuildConfig.API_KEY)

    suspend fun getWeatherByCoord(lat: String, lon: String): Response<WeatherModel> =
        weatherApi.getWeatherByCoordAsync(lat, lon,  BuildConfig.API_KEY)

    suspend fun getWeatherByZipCode(zipCode: String): Response<WeatherModel> =
        weatherApi.getWeatherByZipCodeAsync(zipCode,  BuildConfig.API_KEY)
}