package com.jgeniselli.rgbcontroller.color.control

import com.jgeniselli.rgbcontroller.color.control.RGBControllerViewModel
import com.jgeniselli.rgbcontroller.data.source.Color
import com.jgeniselli.rgbcontroller.data.source.ColorRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class RGBControllerViewModelTest {

    private val deviceAddress: String = "123:456:789"
    private lateinit var repository: ColorRepository
    private lateinit var viewModel: RGBControllerViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        repository = mockk(relaxed = true)
        viewModel =
            RGBControllerViewModel(
                repository,
                deviceAddress
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `WHEN start is called THEN connect to device in repository`() = runBlocking {
        viewModel.onStart()
        coVerify { repository.connectToDevice(deviceAddress) }
    }

    @Test
    fun `WHEN lifecycle is cleared THEN call repository disconnect`() = runBlocking {
        viewModel.onCleared()
        coVerify { repository.disconnect() }
    }

    @Test
    fun `WHEN color apply is called THEN delegate it to repository`() = runBlocking {
        mockkObject(Color.Companion)
        val mockColor = mockk<Color>()
        every { Color.create(30, 40, 50) } returns mockColor

        viewModel.onApplyClicked(30, 40, 50)

        coVerify { Color.create(30, 40, 50) }
        coVerify { repository.applyColor(mockColor) }
    }
}