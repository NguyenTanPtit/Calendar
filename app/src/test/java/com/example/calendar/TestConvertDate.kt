package com.example.calendar

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class TestConvertDate {
    private val c = ConvertDate()
    @Test
    fun testConvertJd(){
        assertEquals(c.jdFromDate(6,12,2001),2452250)
    }

    @Test
    fun testJdToDate(){
        assertEquals(c.jdToDate(2452250).contentToString(),
            intArrayOf(6,12,2001).contentToString())
    }

    @Test
    fun testSunLongItude(){
        assertEquals(c.sunLongitude8(12.3),257.27060360321775,0.0)
    }

    @Test
    fun testSunLongItude2(){
        assertEquals(c.sunLongItude(12.3),257.27060360321775,0.0)
    }

    @Test
    fun testGetSunLongitude(){
        assertEquals(c.getSunLongitude(6,7.0),250.25086164101958,0.0)
    }

    @Test
    fun testNewMoonDay(){
        assertEquals(c.newMoonDay(6),2415197.561424751,0.0)
    }

    @Test
    fun testGetNewMoonDay(){
        assertEquals(c.getNewMoonDay(6,7.0),2415198)
    }

    @Test
    fun testGetLunarMonth11(){
        assertEquals(c.getLunarMonth11(2001,7.0),2452259)
    }
    @Test
    fun testGetLeapMonthOffset(){
        assertEquals(c.getLeapMonthOffset(2001,7.0),11)
    }

    @Test
    fun testConvert(){
        assertEquals(c.convertSolar2Lunar(6,12,2001,7.0).contentToString(),
            intArrayOf(22,10,2001,0).contentToString())
    }

}