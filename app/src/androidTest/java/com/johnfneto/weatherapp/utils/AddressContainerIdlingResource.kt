package com.johnfneto.weatherapp.utils

import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback

class AddressContainerIdlingResource(
    var AddressContainer: LinearLayout,
    var mTag: String
) : IdlingResource {

    @Volatile
    private var resourceCallback: ResourceCallback? = null

    override fun getName(): String {
        return AddressContainerIdlingResource::class.java.name + ":" + mTag
    }

    // Checks if AddressContainer is visible, then WeatherScreenFragment has finished receiving data from the API
    override fun isIdleNow(): Boolean {
        val idle = AddressContainer.isVisible
        if (idle) {
            resourceCallback!!.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(resourceCallback: ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

}