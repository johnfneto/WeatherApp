package com.johnfneto.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocationDao {
    @Query("SELECT * FROM database_locations ORDER BY date DESC")
    fun getLocationsList(): LiveData<List<WeatherLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(vararg location: WeatherLocation)

    @Update
    suspend fun updateLocation(vararg location: WeatherLocation)

    @Query("DELETE FROM database_locations WHERE :id = uid")
    suspend fun deleteLocation(id: Int)

    @Query("DELETE FROM database_locations")
    suspend fun deleteAll()
}