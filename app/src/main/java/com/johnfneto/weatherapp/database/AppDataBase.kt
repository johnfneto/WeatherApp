package com.johnfneto.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.johnfneto.weatherapp.models.WeatherLocation
import com.johnfneto.weatherapp.utils.DATABASE_NAME
import kotlinx.coroutines.CoroutineScope

/**
 * The Room database for weather location
 */
@Database(entities = [WeatherLocation::class], version = 1, exportSchema = false)
abstract class AppDataBase
    : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DATABASE_NAME
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}