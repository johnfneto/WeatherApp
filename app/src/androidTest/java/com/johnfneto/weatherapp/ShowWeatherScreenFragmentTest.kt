package com.johnfneto.weatherapp

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.johnfneto.weatherapp.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShowWeatherScreenFragmentTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun jumpToWeatherScreenFragment() {
        activityTestRule.activity.apply {
            runOnUiThread {
                val bundle = Bundle().apply { putString("locationName", MY_CITY) }
                findNavController(R.id.nav_host_fragment).navigate(
                    R.id.weatherScreenFragment,
                    bundle
                )
            }
        }
        onView(withId(R.id.searchText)).check(matches(isDisplayed()))
    }

    @Test
    fun clickAndJumpToWeatherScreenFragment() {
        onView(withId(R.id.recentSearchesButton)).perform(click())
        onView(withId(R.id.recycleListView)).check(matches(isDisplayed()))
    }
}