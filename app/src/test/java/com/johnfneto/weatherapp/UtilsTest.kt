package com.johnfneto.weatherapp

import com.johnfneto.weatherapp.utils.Utils
import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun formatTime_isCorrect() {
        assertEquals("07:30 PM", Utils.formatTime(1583137841))
    }

    @Test
    fun formatDate_isCorrect() {
        assertEquals("02 Mar 2020, 03:31 PM", Utils.formatDate(1583123469811))
    }

    @Test
    fun formatShorterDate_isCorrect() {
        assertEquals("02 Mar, 03:31 PM", Utils.formatShorterDate(1583123469811))
    }

}