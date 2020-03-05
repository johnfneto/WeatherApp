package com.johnfneto.weatherapp.models

import com.google.gson.annotations.SerializedName
import com.johnfneto.weatherapp.utils.OpenForTesting
import com.johnfneto.weatherapp.utils.Utils

@OpenForTesting
data class Sys (

	@SerializedName("type") val type : Int,
	@SerializedName("id") val id : Int,
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