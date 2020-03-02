package com.johnfneto.weatherapp

import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.johnfneto.weatherapp.ui.MainActivity
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecentSearchesAdapterTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun gotoRecentSearchesFragment() {
        activityTestRule.activity.apply {
            runOnUiThread {
                findNavController(R.id.nav_host_fragment).navigate(R.id.recentSearchesFragment)
            }
        }
    }

    @Test
    fun testCaseForRecyclerClick() {
        onView(withId(R.id.recycleListView))
            .inRoot(RootMatchers.withDecorView(
                Matchers.`is`(activityTestRule.activity.window.decorView)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    @Test
    fun jumpToWeatherScreenFragment() {
        onView(withId(R.id.recycleListView)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText("Bondi, AU")),
                click()
            )
        )
    }
}