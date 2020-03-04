package com.johnfneto.weatherapp

import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.onView
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
class ShowRecentLocationsTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun jumpToWeatherScreenFragment() {
        activityTestRule.activity.apply {
            runOnUiThread {
                findNavController(R.id.nav_host_fragment).navigate(R.id.recentSearchesFragment)
            }
        }
        onView(withId(R.id.recycleListView)).check(matches(isDisplayed()))
    }
}