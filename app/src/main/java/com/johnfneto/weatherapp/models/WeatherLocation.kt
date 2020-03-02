package com.johnfneto.weatherapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.johnfneto.weatherapp.utils.DATABASE_NAME
import com.johnfneto.weatherapp.utils.Utils

@Entity(tableName = DATABASE_NAME, indices = [Index(value = ["city"], unique = true)])
data class WeatherLocation(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "city") var city: String,
    @ColumnInfo(name = "date") var date: Long?

    ) {

    val formattedDate
        get() =  Utils.formatShorterDate(date!!)
}