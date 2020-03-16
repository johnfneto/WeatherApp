package com.johnfneto.weatherapp

import android.content.Context
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.johnfneto.weatherapp.database.AppDataBase
import com.johnfneto.weatherapp.database.LocationDao
import com.johnfneto.weatherapp.ui.MainActivity
import com.johnfneto.weatherapp.utils.AddressContainerIdlingResource
import com.johnfneto.weatherapp.utils.RecyclerViewMatcher
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

const val MY_CITY = "Bondi, AU"

@Suppress("BlockingMethodInNonBlockingContext")
@RunWith(AndroidJUnit4::class)
class RecentSearchesAdapterTest {

    @Rule
    @JvmField
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var locationDao: LocationDao
    private lateinit var db: AppDataBase

    @Before
    fun gotoRecentSearchesFragment() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        locationDao = db.locationDao()

        activityTestRule.activity.apply {
            runOnUiThread {
                findNavController(R.id.nav_host_fragment).navigate(R.id.recentSearchesFragment)
            }
        }
    }

    @Test
    fun testCaseForRecyclerClick() {
        onView(withId(R.id.recycleListView))
            .inRoot(
                RootMatchers.withDecorView(
                    Matchers.`is`(activityTestRule.activity.window.decorView)
                )
            )
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
    }

    /*@Test
    @Throws(Exception::class)
    fun insertLocation() = runBlocking {
        val location = WeatherLocation(
            null,
            MY_CITY,
            Date().time
        )
        locationDao.saveLocation(location)
    }*/

    @Test
    fun jumpToWeatherScreenFragment() {
        onView(RecyclerViewMatcher(R.id.recycleListView).atPosition(0))
            .check(matches(hasDescendant(withText(MY_CITY))))

        onView(withId(R.id.recycleListView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        val addressContainer =
            activityTestRule.activity.findViewById<LinearLayout>(R.id.addressContainer)
        val addressContainerIdlingResource = AddressContainerIdlingResource(addressContainer, "tag")

        IdlingRegistry.getInstance().register(addressContainerIdlingResource)
        onView(withId(R.id.address)).check(matches(withText(MY_CITY)))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}