package com.jgeniselli.rgbcontroller.toolbox.livedata

import org.junit.Assert.*
import org.junit.Test

class EventTest {

    @Test
    fun `WHEN content requested at first time THEN return content and block next retrieval`() {
        val event = Event("something")
        assertFalse(event.hasBeenHandled)
        assertEquals("something", event.peekContent())
        assertEquals("something", event.getContentIfNotHandled())
        assertTrue(event.hasBeenHandled)
        assertNull(event.getContentIfNotHandled())
        assertEquals("something", event.peekContent())
    }
}