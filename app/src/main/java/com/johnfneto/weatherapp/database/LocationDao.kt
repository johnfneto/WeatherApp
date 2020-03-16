package com.johnfneto.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.johnfneto.weatherapp.models.WeatherModel
import com.johnfneto.weatherapp.utils.DATABASE_NAME

@Dao
interface LocationDao {

    @Query("SELECT * FROM $DATABASE_NAME ORDER BY updated DESC")
    fun getLocationsList(): LiveData<List<WeatherModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(vararg location: WeatherModel)

    @Update
    suspend fun updateLocation(vararg location: WeatherModel)

    @Query("DELETE FROM $DATABASE_NAME WHERE :id = uid")
    suspend fun deleteLocation(id: Int)

    @Query("DELETE FROM $DATABASE_NAME")
    suspend fun deleteAll()
}