package com.johnfneto.weatherapp.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.johnfneto.weatherapp.R
import com.johnfneto.weatherapp.models.WeatherModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Utils {

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network)
        if (network == null || networkCapabilities == null) {
            Toast.makeText(context, context.resources.getString(R.string.internet_unavailable), Toast.LENGTH_SHORT).show()
            return false
        }
        networkCapabilities.apply {
            return when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> {
                    Toast.makeText(context, context.resources.getString(R.string.internet_unavailable), Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }
    }

    fun buildErrorResponse(message: String?) = Response.error<WeatherModel>(
        400,
        "{\"key\":[$message]}"
            .toResponseBody("application/json".toMediaTypeOrNull())
    )

    fun hideKeyboard(activity: Activity?) {
        if (activity != null) {
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            inputMethodManager(activity)
                .hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun inputMethodManager(activity: Activity)
        = activity.applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    @JvmStatic
    fun getCompassValue(bearing: Int): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        return "from " + directions[ (((bearing % 360) / 45).toDouble().roundToInt() % 8) ]
    }

    @JvmStatic
    fun getWindDirect(bearing: Int): String {
        val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        return directions[ (((bearing % 360) / 45).toDouble().roundToInt() % 8) ]
    }

    @JvmStatic
    fun formatTemp(temperature: Double): String {
        return temperature.toInt().toString()
    }

    @JvmStatic
    fun getWindSpeed(speed: Double) = (String.format("%.0f", speed * 3.6))

    @JvmStatic
    fun formatTime(time: Int): String = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(time*1000L))

    @JvmStatic
    fun formatDate(date: Long): String = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH).format(Date(date))

    @JvmStatic
    fun formatShorterDate(date: Long): String = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH).format(Date(date))

    private fun convertKelvinToCelsius(kelvin: Double) = kelvin - 273.15

    private fun convertKelvinToFahrenheit(kelvin: Double) = (kelvin - 273.15) * 9/5 + 32

    fun getSelectedTemperatureUnit(context: Context, temp: Double): Double {
        return when (Preferences.getTemperatureUnit(context)) {

            TemperatureUnitsEnum.CELSIUS.name -> convertKelvinToCelsius(temp)
            TemperatureUnitsEnum.FAHRENHEIT.name -> convertKelvinToFahrenheit(temp)

            else -> 0.0
        }
    }
}
