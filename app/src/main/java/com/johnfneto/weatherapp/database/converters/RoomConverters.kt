package com.johnfneto.weatherapp.database.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.johnfneto.weatherapp.models.*
import java.time.Instant
import java.util.*

class RoomConverters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromCalendar(value: Calendar?): Long {
            return value!!.timeInMillis
        }

        @TypeConverter
        @JvmStatic
        fun toCalendar(value: Long): Calendar {
            val cal = Calendar.getInstance()
            cal.timeInMillis = value
            return cal
        }


        @RequiresApi(Build.VERSION_CODES.O)
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Instant?): Long {
            return value!!.toEpochMilli()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @TypeConverter
        @JvmStatic
        fun toInstant(value: Long): Instant {
            return Instant.ofEpochMilli(value)
        }

        @TypeConverter
        @JvmStatic
        fun stringToCoords(json: String?): Coord? {
            val type = object : TypeToken<Coord>() {}.type
            return Gson().fromJson<Coord>(json, type)
        }

        @TypeConverter
        @JvmStatic
        fun coordsToString(nestedData: Coord?): String? {
            val type = object : TypeToken<Coord>() {}.type
            return Gson().toJson(nestedData, type)
        }

        @TypeConverter
        @JvmStatic
        fun toWeather(json: String): List<Weather> {
            val type = object : TypeToken<List<Weather>>() {}.type
            return Gson().fromJson(json, type)
        }

        @TypeConverter
        @JvmStatic
        fun fromWeather(torrent: List<Weather>): String {
            val type = object: TypeToken<List<Weather>>() {}.type
            return Gson().toJson(torrent, type)
        }

        @TypeConverter
        @JvmStatic
        fun toMain(json: String): Main {
            val type = object : TypeToken<Main>() {}.type
            return Gson().fromJson(json, type)
        }

        @TypeConverter
        @JvmStatic
        fun fromMain(torrent: Main): String {
            val type = object: TypeToken<Main>() {}.type
            return Gson().toJson(torrent, type)
        }

        @TypeConverter
        @JvmStatic
        fun toWind(json: String): Wind {
            val type = object : TypeToken<Wind>() {}.type
            return Gson().fromJson(json, type)
        }

        @TypeConverter
        @JvmStatic
        fun fromWind(torrent: Wind): String {
            val type = object: TypeToken<Wind>() {}.type
            return Gson().toJson(torrent, type)
        }

        @TypeConverter
        @JvmStatic
        fun toClouds(json: String): Clouds {
            val type = object : TypeToken<Clouds>() {}.type
            return Gson().fromJson(json, type)
        }

        @TypeConverter
        @JvmStatic
        fun fromClouds(torrent: Clouds): String {
            val type = object: TypeToken<Clouds>() {}.type
            return Gson().toJson(torrent, type)
        }

        @TypeConverter
        @JvmStatic
        fun toSys(json: String): Sys {
            val type = object : TypeToken<Sys>() {}.type
            return Gson().fromJson(json, type)
        }

        @TypeConverter
        @JvmStatic
        fun fromSys(torrent: Sys): String {
            val type = object: TypeToken<Sys>() {}.type
            return Gson().toJson(torrent, type)
        }


    }
}