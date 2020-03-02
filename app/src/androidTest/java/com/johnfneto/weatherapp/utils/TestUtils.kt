package com.johnfneto.weatherapp.utils

import com.johnfneto.weatherapp.models.WeatherLocation
import java.util.*

/**
 * [WeatherLocation] objects used for tests.
 */
val testLocations = arrayListOf(
    WeatherLocation(null, "city_1", Date().time),
    WeatherLocation(null, "city_2", Date().time),
    WeatherLocation(null, "city_3", Date().time)
)
val testLocation = testLocations[0]

/**
 * [Calendar] object used for tests.
 */
val testCalendar: Calendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, 1998)
    set(Calendar.MONTH, Calendar.SEPTEMBER)
    set(Calendar.DAY_OF_MONTH, 4)
}