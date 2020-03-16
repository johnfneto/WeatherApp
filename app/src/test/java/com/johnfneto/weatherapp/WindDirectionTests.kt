package com.johnfneto.weatherapp

import com.johnfneto.weatherapp.utils.Utils
import org.junit.Test

import org.junit.Assert.*

class WindDirectionTests {

    @Test
    fun calculateWindDirectionETest() {
        assertEquals("E", Utils.getCompassValue(90))
    }

    @Test
    fun calculateWindDirectionSTest() {
        assertEquals("S", Utils.getCompassValue(180))
    }

    @Test
    fun calculateWindDirectionWTest() {
        assertEquals("W", Utils.getCompassValue(270))
    }

    @Test
    fun calculateWindDirectionNTest() {
        assertEquals("N", Utils.getCompassValue(0))
    }
    @Test
    fun calculateWindDirectionNETest() {
        assertEquals("NE", Utils.getCompassValue(45))
    }

    @Test
    fun calculateWindDirectionNE2Test() {
        assertEquals("NE", Utils.getCompassValue(46))
    }

    @Test
    fun calculateWindDirectionNE3Test() {
        assertEquals("NE", Utils.getCompassValue(89))
    }

    @Test
    fun calculateWindDirectionSWTest() {
        assertEquals("SW", Utils.getCompassValue(260))
    }
}