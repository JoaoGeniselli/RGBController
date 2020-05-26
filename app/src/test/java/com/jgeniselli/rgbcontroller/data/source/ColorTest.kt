package com.jgeniselli.rgbcontroller.data.source

import com.jgeniselli.rgbcontroller.data.source.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorTest {

    @Test(expected = IllegalArgumentException::class)
    fun `GIVEN invalid value WHEN create color THEN throw error`() {
        Color.create(256, 90, 90)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `GIVEN negative value WHEN create color THEN throw error`() {
        Color.create(90, -15, 90)
    }

    @Test
    fun `GIVEN valid values WHEN create color THEN create it`() {
        val color = Color.create(100, 105, 110)
        assertEquals(100, color.red)
        assertEquals(105, color.green)
        assertEquals(110, color.blue)
    }

    @Test
    fun `WHEN transform into light pattern called THEN return correctly formatted string`() {
        val color = Color.create(100, 30, 1)
        assertEquals("100|030|001", color.toLightDevicePattern())
    }
}