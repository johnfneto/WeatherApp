package com.johnfneto.weatherapp.models

import com.google.gson.annotations.SerializedName
import com.johnfneto.weatherapp.utils.OpenForTesting

@OpenForTesting
data class Weather (

	@SerializedName("id") val id : Int,
	@SerializedName("main") val main : String,
	@SerializedName("description") val description : String,
	@SerializedName("icon") val icon : String
)