package com.johnfneto.weatherapp.models

import com.google.gson.annotations.SerializedName
import com.johnfneto.weatherapp.utils.OpenForTesting

@OpenForTesting
data class Clouds (

	@SerializedName("all") val all : Int
)