package com.example.calendar

import org.junit.Assert.*
import org.junit.Test
import java.util.TimeZone

class TestGetTimeZone {
    private fun getZones(country: String): List<String> {
        val zones: MutableList<String> = ArrayList()
        for (i in TimeZone.getAvailableIDs()) {
            if (i.startsWith(country) and i.contains("Mountain")) {
                zones.add(i)
            }
        }
        return zones
    }

    @Test
    fun testGetTimeZone(){
        println(getZones("US"))
    }
}