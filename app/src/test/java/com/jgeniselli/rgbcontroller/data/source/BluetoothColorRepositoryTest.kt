package com.jgeniselli.rgbcontroller.data.source

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.ParcelUuid
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.OutputStream
import java.util.*

class BluetoothColorRepositoryTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private val deviceAddress = "123:456:789"
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var socket: BluetoothSocket
    private lateinit var output: OutputStream
    private lateinit var device: BluetoothDevice
    private lateinit var repository: BluetoothColorRepository

    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        output = mockk(relaxed = true)
        socket = mockk(relaxed = true) { every { outputStream } returns output }
        val mockUuid = mockk<UUID>()
        val parcelUuid = mockk<ParcelUuid> { every { uuid } returns mockUuid }
        device = mockk {
            every { uuids } returns arrayOf(parcelUuid)
            every { createRfcommSocketToServiceRecord(mockUuid) } returns socket
        }
        bluetoothAdapter = mockk {
            every { getRemoteDevice(deviceAddress) } returns device
        }
        repository = BluetoothColorRepository(bluetoothAdapter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `GIVEN valid device address WHEN connect to device is called THEN load device and connect socket`() = runBlocking {
        repository.connectToDevice(deviceAddress)

        verify { bluetoothAdapter.getRemoteDevice(deviceAddress) }
        verify { device.uuids }
        verify { device.createRfcommSocketToServiceRecord(any()) }
        verify { socket.connect() }
    }

    @Test
    fun `GIVEN invalid address WHEN connect to device is called THEN ignore connection`() = runBlocking {
        every { bluetoothAdapter.getRemoteDevice(deviceAddress) } returns null

        repository.connectToDevice(deviceAddress)

        verify(exactly = 0) { device.uuids }
        verify(exactly = 0) { device.createRfcommSocketToServiceRecord(any()) }
        verify(exactly = 0) { socket.connect() }
    }

    @Test
    fun `GIVEN valid output stream WHEN apply color is called THEN delegate color pattern to output stream`() = runBlocking {
        val pattern = "123|123|123"
        val color = mockk<Color> {
            every { toLightDevicePattern() } returns pattern
        }

        val spy = spyk(repository, recordPrivateCalls = true)
        every { spy getProperty "bluetoothSocket" } returns socket

        spy.applyColor(color)

        verify { output.write(any<ByteArray>()) }
        verify { color.toLightDevicePattern() }
    }

    @Test
    fun `GIVEN null socket or output stream WHEN apply color is called THEN ignore writing pattern`() = runBlocking {
        val pattern = "123|123|123"
        val color = mockk<Color> {
            every { toLightDevicePattern() } returns pattern
        }

        val spy = spyk(repository, recordPrivateCalls = true)
        every { spy getProperty "bluetoothSocket" } returns socket
        every { socket.outputStream } returns null

        spy.applyColor(color)

        every { spy getProperty "bluetoothSocket" } returns null

        spy.applyColor(color)

        verify(exactly = 0) { output.write(any<ByteArray>()) }
        verify(exactly = 0) { color.toLightDevicePattern() }
    }

    @Test
    fun `GIVEN still connected socket WHEN disconnect is called THEN close it`() = runBlocking {
        every { socket.isConnected } returns true
        val spy = spyk(repository, recordPrivateCalls = true)
        every { spy getProperty "bluetoothSocket" } returns socket

        spy.disconnect()

        verify(exactly = 1) { socket.close() }
    }

    @Test
    fun `GIVEN null or already disconnected socket WHEN disconnect is called THEN ignore it`() = runBlocking {
        every { socket.isConnected } returns false
        val spy = spyk(repository, recordPrivateCalls = true)
        every { spy getProperty "bluetoothSocket" } returns socket

        spy.disconnect()

        every { spy getProperty "bluetoothSocket" } returns null

        spy.disconnect()

        verify(exactly = 0) { socket.close() }
    }


}