package com.johnfneto.weatherapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.johnfneto.weatherapp.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class WeatherScreenFragmentTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun clickRecentSearches_OpensRecentLocationsList() {

        onView(withId(R.id.recentSearchesButton)).perform(ViewActions.click())

        onView(withId(R.id.recycleListView))
            .check(matches(isDisplayed()))
    }
}