package com.johnfneto.weatherapp

import com.johnfneto.weatherapp.models.WeatherModel
import com.johnfneto.weatherapp.weather_api.Service
import com.johnfneto.weatherapp.weather_api.WeatherAPI
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mockito.mock
import retrofit2.Response

@ExperimentalCoroutinesApi
class WeatherApiTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private lateinit var weatherApi: WeatherAPI

    @Before
    fun setup() {
        weatherApi = Service().createService(WeatherAPI::class.java)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun getWeatherInfoByCityTest() = testScope.runBlockingTest {
        val weatherModel = mock(WeatherModel::class.java)

        val mockRepo = mock<WeatherModel> {
            onBlocking {
                weatherApi.getWeatherByCityAsync("San Francisco", "metric", BuildConfig.API_KEY)
            } doReturn Response.success(weatherModel)
        }

        Assert.assertEquals(mockRepo.name, "San Francisco")
        Assert.assertEquals(mockRepo.sys.country, "US")
    }

    @Test
    fun getWeatherInfoByZipCodeTest() = testScope.runBlockingTest {
        val weatherModel = mock(WeatherModel::class.java)

        val mockRepo = mock<WeatherModel> {
            onBlocking {
                weatherApi.getWeatherByZipCodeAsync("91932", "metric", BuildConfig.API_KEY)
            } doReturn Response.success(weatherModel)
        }

        Assert.assertEquals(mockRepo.name, "San Diego")
        Assert.assertEquals(mockRepo.sys.country, "US")
    }
}