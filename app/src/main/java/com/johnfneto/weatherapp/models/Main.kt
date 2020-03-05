package com.johnfneto.weatherapp.models

import com.google.gson.annotations.SerializedName
import com.johnfneto.weatherapp.utils.OpenForTesting
import com.johnfneto.weatherapp.utils.Utils

@OpenForTesting
data class Main (

	@SerializedName("temp") val temp : Double,
	@SerializedName("feels_like") val feels_like : Double,
	@SerializedName("temp_min") val temp_min : Double,
	@SerializedName("temp_max") val temp_max : Double,
	@SerializedName("pressure") val pressure : Int,
	@SerializedName("humidity") val humidity : Int
)
{
	val formattedTemperature
			get() = Utils.formatTemp(temp)

	val formattedTempMin
			get() = Utils.formatTemp(temp_min)

	val formattedTempMax
			get() = Utils.formatTemp(temp_max)
}