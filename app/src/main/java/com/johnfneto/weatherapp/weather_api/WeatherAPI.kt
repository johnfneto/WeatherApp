package com.johnfneto.weatherapp.weather_api

import com.johnfneto.weatherapp.models.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather")
    suspend fun getWeatherByCityAsync(@Query("q") city: String, @Query("units") units: String, @Query("appid") appid: String): Response<WeatherModel>

    @GET("weather")
    suspend fun getWeatherByZipCodeAsync(@Query("zip") zipCode: String, @Query("units") units: String, @Query("appid") appid: String): Response<WeatherModel>

    @GET("weather")
    suspend fun getWeatherByCoordAsync(@Query("lat") lat: String, @Query("lon") lon: String, @Query("units") units: String, @Query("appid") appid: String): Response<WeatherModel>
}