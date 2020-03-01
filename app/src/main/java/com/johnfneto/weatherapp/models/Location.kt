package com.johnfneto.weatherapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["city"], unique = true)])
data class Location(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "city") var city: String,
    @ColumnInfo(name = "date") var date: Long?

    )