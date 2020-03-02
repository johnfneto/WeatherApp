package com.johnfneto.weatherapp

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.johnfneto.weatherapp.ui.MainActivity
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class WeatherScreenFragmentTest{

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun clickRecentSearches_OpensRecentLocationsList() {

        Espresso.onView(ViewMatchers.withId(R.id.recentSearchesButton)).perform(ViewActions.click())

        // Then the RecycleView should show the Locations List
        Espresso.onView(ViewMatchers.withId(R.id.recycleListView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}