package com.johnfneto.weatherapp

import android.app.Activity
import android.app.Application
import android.content.Context
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

lateinit var appContext: Context

class WeatherApplication : Application() {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}