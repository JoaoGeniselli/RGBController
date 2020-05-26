package com.jgeniselli.rgbcontroller.devices

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jgeniselli.rgbcontroller.data.source.DevicesRepository
import com.jgeniselli.rgbcontroller.data.source.PairedDevice
import com.jgeniselli.rgbcontroller.util.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class PairedDevicesViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var repository: DevicesRepository
    private lateinit var viewModel: PairedDevicesViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        repository = mockk(relaxed = true)
        viewModel = PairedDevicesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `WHEN load devices is called THEN request it from repository`() = runBlocking {
        val deviceA = mockk<PairedDevice> { every { name } returns "A" }
        val deviceB = mockk<PairedDevice> { every { name } returns "B" }

        coEvery { repository.findAllDevices() } returns listOf(deviceA, deviceB)

        val progressValues = mutableListOf<Boolean>()
        val progressObserver: (Boolean) -> Unit = { progressValues.add(it) }
        viewModel.progressIsVisible.observeForever(progressObserver)

        viewModel.loadDevices()

        viewModel.progressIsVisible.removeObserver(progressObserver)
        assertEquals(2, progressValues.size)
        assertEquals(true, progressValues[0])
        assertEquals(false, progressValues[1])

        val loadedDevices = viewModel.devices.getOrAwaitValue()
        assertEquals(2, loadedDevices.size)
        assertEquals("A", loadedDevices[0])
        assertEquals("B", loadedDevices[1])

        coVerify { repository.findAllDevices() }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `GIVEN invalid position WHEN device is clicked THEN throw error`() = runBlocking {
        val deviceA = mockk<PairedDevice>()

        every { deviceA.name } returns "A"
        coEvery { repository.findAllDevices() } returns listOf(deviceA)

        viewModel.onDeviceClicked(2)
    }

    @Test
    fun `GIVEN valid position WHEN device is clicked THEN redirect to control`() = runBlocking {
        val deviceA = mockk<PairedDevice> {
            every { name } returns "A"
            every { address } returns "123:456:789"
        }
        coEvery { repository.findAllDevices() } returns listOf(deviceA)

        viewModel.loadDevices()
        viewModel.devices.getOrAwaitValue()
        viewModel.onDeviceClicked(0)

        val redirectEvent = viewModel.redirectToControl.getOrAwaitValue()

        assertEquals("123:456:789", redirectEvent.getContentIfNotHandled())
    }


}