package com.johnfneto.weatherapp.models

import com.google.gson.annotations.SerializedName
import com.johnfneto.weatherapp.utils.OpenForTesting
import com.johnfneto.weatherapp.utils.Utils

@OpenForTesting
data class Wind (

	@SerializedName("speed") val speed : Double,
	@SerializedName("deg") val deg : Int
)
{
	val formattedWindSpeed
		get() = Utils.getWindSpeed(speed)

	val formattedBearing
		get() = Utils.formatBearing(deg)
}