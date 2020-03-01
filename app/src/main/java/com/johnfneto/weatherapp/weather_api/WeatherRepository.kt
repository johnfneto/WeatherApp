package com.johnfneto.weatherapp.weather_api

import androidx.lifecycle.MutableLiveData
import com.johnfneto.weatherapp.models.WeatherModel

object WeatherRepository {

    var weatherData = MutableLiveData<WeatherModel>()

    var errorStatus: MutableLiveData<Boolean> = MutableLiveData()
}