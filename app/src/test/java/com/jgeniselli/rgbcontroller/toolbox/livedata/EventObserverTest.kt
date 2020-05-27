package com.jgeniselli.rgbcontroller.toolbox.livedata

import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class EventObserverTest {

    @Test
    fun `GIVEN never handled event WHEN value has changed THEN call listener`() {
        val listener = mockk<(String) -> Unit>(relaxed = true)
        val event = mockk<Event<String>> {
            every { getContentIfNotHandled() } returns "something"
        }
        val observer = EventObserver(listener)

        observer.onChanged(event)

        verify { listener("something") }
    }

    @Test
    fun `GIVEN already handled event WHEN value has changed THEN avoid listener call`() {
        val listener = mockk<(String) -> Unit>(relaxed = true)
        val event = mockk<Event<String>> {
            every { getContentIfNotHandled() } returns null
        }
        val observer = EventObserver(listener)

        observer.onChanged(event)

        verify { listener(any()) wasNot called }
    }

    @Test
    fun `GIVEN null event WHEN value has changed THEN avoid listener call`() {
        val listener = mockk<(String) -> Unit>(relaxed = true)
        val observer = EventObserver(listener)

        observer.onChanged(null)

        verify { listener(any()) wasNot called }
    }
}