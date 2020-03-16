package com.johnfneto.weatherapp.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import com.johnfneto.weatherapp.R
import com.johnfneto.weatherapp.appContext
import com.johnfneto.weatherapp.utils.DATABASE_NAME
import com.johnfneto.weatherapp.utils.Preferences
import com.johnfneto.weatherapp.utils.TemperatureUnitsEnum
import com.johnfneto.weatherapp.utils.Utils
import com.johnfneto.weatherapp.utils.Utils.formatTemp
import com.johnfneto.weatherapp.utils.Utils.getSelectedTemperatureUnit

@Entity(tableName = DATABASE_NAME, primaryKeys = ["uid"], indices = [Index(value = ["name", "country"], unique = true)], inheritSuperIndices = true)
data class WeatherModel @RequiresApi(Build.VERSION_CODES.O) constructor(

    @field:SerializedName("uid") var uid: Int?,
    @field:SerializedName("updated") var updated: Long,
    @field:SerializedName("coord") val coord: Coord,
    @field:SerializedName("weather") val weather: List<Weather>,
    @field:SerializedName("base") val base: String,
    @field:SerializedName("main") val main: Main,
    @field:SerializedName("visibility") val visibility: Int,
    @field:SerializedName("wind") val wind: Wind,
    @field:SerializedName("clouds") val clouds: Clouds,
    @field:SerializedName("dt") val dt: Int,
    @Embedded
    @field:SerializedName("sys") val sys: Sys,
    @field:SerializedName("timezone") val timezone: Int,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String = "",
    @field:SerializedName("cod") val cod: Int
) {
    val formattedDate
        get() =  Utils.formatShorterDate(updated)
}

data class Coord (

    @SerializedName("lon") val lon : Double,
    @SerializedName("lat") val lat : Double
)

data class Weather (

    @SerializedName("id") val id : Int,
    @SerializedName("main") val main : String,
    @SerializedName("description") val description : String,
    @SerializedName("icon") val icon : String
)

data class Main (

    @SerializedName("temp") val temp : Double,
    @SerializedName("feels_like") val feels_like : Double,
    @SerializedName("temp_min") val temp_min : Double,
    @SerializedName("temp_max") val temp_max : Double,
    @SerializedName("pressure") val pressure : Int,
    @SerializedName("humidity") val humidity : Int
)
{
    val formattedTemperature: String
        get() {
            val unitType =
            when (Preferences.getTemperatureUnit(appContext)) {
                TemperatureUnitsEnum.FAHRENHEIT.name -> appContext.getString(R.string.temp_f)
                TemperatureUnitsEnum.CELSIUS.name -> appContext.getString(R.string.temp_c)
                else -> ""
            }

            return String.format(unitType, formatTemp(getSelectedTemperatureUnit(appContext, temp)))
        }

    val formattedTempMin: String
        get() {
            val unitType =
                when (Preferences.getTemperatureUnit(appContext)) {
                    TemperatureUnitsEnum.FAHRENHEIT.name -> appContext.getString(R.string.min_temp_f)
                    TemperatureUnitsEnum.CELSIUS.name -> appContext.getString(R.string.min_temp_c)
                    else -> ""
                }

            return String.format(unitType, formatTemp(getSelectedTemperatureUnit(appContext, temp_min)))
        }

    val formattedTempMax: String
        get() {
            val unitType =
                when (Preferences.getTemperatureUnit(appContext)) {
                    TemperatureUnitsEnum.FAHRENHEIT.name -> appContext.getString(R.string.max_temp_f)
                    TemperatureUnitsEnum.CELSIUS.name -> appContext.getString(R.string.min_temp_c)
                    else -> ""
                }

            return String.format(unitType, formatTemp(getSelectedTemperatureUnit(appContext, temp_max)))
        }
}

data class Wind (

    @SerializedName("speed") val speed : Double,
    @SerializedName("deg") val deg : Int
)
{
    val formattedWindSpeed
        get() = Utils.getWindSpeed(speed)

    val formattedBearing
        get() = Utils.getCompassValue(deg)

    val windDirection
        get() = deg + 180
}

data class Clouds (

    @SerializedName("all") val all : Int
)

data class Sys (

    @SerializedName("type") val type : Int,
    @SerializedName("country") val country : String = "",
    @SerializedName("sunrise") val sunrise : Int,
    @SerializedName("sunset") val sunset : Int

)
{
    val formattedSunrise
        get() = Utils.formatTime(sunrise)

    val formattedSunset
        get() = Utils.formatTime(sunset)
}