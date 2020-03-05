package com.johnfneto.weatherapp.models

import com.google.gson.annotations.SerializedName
import com.johnfneto.weatherapp.utils.OpenForTesting

@OpenForTesting
data class Coord (

	@SerializedName("lon") val lon : Double,
	@SerializedName("lat") val lat : Double
)