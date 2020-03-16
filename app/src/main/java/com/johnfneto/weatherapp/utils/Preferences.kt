package com.johnfneto.weatherapp.utils

import android.content.Context
import android.content.pm.PackageManager

object Preferences {
    private fun put(
        context: Context,
        name: PreferenceNameEnum,
        value: String?
    ) {
        var packageName = ""
        try {
            packageName =
                context.packageManager.getPackageInfo(context.packageName, 0).packageName
        } catch (e1: PackageManager.NameNotFoundException) {
            e1.printStackTrace()
        }
        val sharedPreferences = context.getSharedPreferences(
            packageName, Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putString(packageName + name, value).apply()
    }

    private operator fun get(context: Context, name: PreferenceNameEnum): String? {
        var packageName = ""
        try {
            packageName =
                context.packageManager.getPackageInfo(context.packageName, 0).packageName
        } catch (e1: PackageManager.NameNotFoundException) {
            e1.printStackTrace()
        }
        val sharedPreferences = context.getSharedPreferences(
            packageName, Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(packageName + name, "")
    }

    fun setTemperatureFahrenheit(context: Context) {
        put(context, PreferenceNameEnum.TEMPERATURE_UNITS, TemperatureUnitsEnum.FAHRENHEIT.name)
    }

    fun setTemperatureCelsius(context: Context) {
        put(context, PreferenceNameEnum.TEMPERATURE_UNITS, TemperatureUnitsEnum.CELSIUS.name)
    }

    fun getTemperatureUnit(context: Context): String? {
        return get(context, PreferenceNameEnum.TEMPERATURE_UNITS)
    }
}