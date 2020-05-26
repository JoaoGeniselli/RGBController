package com.jgeniselli.rgbcontroller

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BluetoothDevicesRepositoryTest {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var repository: BluetoothDevicesRepository

    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        bluetoothAdapter = mockk(relaxed = true)
        repository = BluetoothDevicesRepository(bluetoothAdapter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `GIVEN no devices available WHEN find is called THEN return empty list`() = runBlocking {
        every { bluetoothAdapter.bondedDevices } returns setOf<BluetoothDevice>()

        val devices = repository.findAllDevices()

        verify { bluetoothAdapter.bondedDevices }
        assertTrue(devices.isEmpty())
    }

    @Test
    fun `GIVEN multiple devices WHEN find is called THEN return valid devices`() = runBlocking {
        val validDevice = mockk<BluetoothDevice>()
        val deviceWithoutAddress = mockk<BluetoothDevice>()
        val deviceWithoutName = mockk<BluetoothDevice>()

        every { validDevice.name } returns "Dev-A"
        every { validDevice.address } returns "123:456:789"

        every { deviceWithoutAddress.name } returns "Dev-B"
        every { deviceWithoutAddress.address } returns null

        every { deviceWithoutName.name } returns null
        every { deviceWithoutName.address } returns "123:456:789"

        every { bluetoothAdapter.bondedDevices } returns setOf(
            validDevice, deviceWithoutAddress, deviceWithoutName
        )

        val devices = repository.findAllDevices()

        verify { bluetoothAdapter.bondedDevices }
        assertEquals(1, devices.size)
        assertEquals("Dev-A", devices.first().name)
        assertEquals("123:456:789", devices.first().address)
    }

}