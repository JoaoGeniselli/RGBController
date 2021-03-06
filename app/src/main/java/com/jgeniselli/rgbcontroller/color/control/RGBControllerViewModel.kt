package com.jgeniselli.rgbcontroller.color.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jgeniselli.rgbcontroller.data.source.Color
import com.jgeniselli.rgbcontroller.data.source.ColorRepository
import kotlinx.coroutines.launch

class RGBControllerViewModel(
    private val colorRepository: ColorRepository,
    private val pairedDeviceAddress: String
) : ViewModel() {

    fun onStart() {
        viewModelScope.launch {
            colorRepository.connectToDevice(pairedDeviceAddress)
        }
    }

    fun onApplyClicked(red: Int, green: Int, blue: Int) {
        viewModelScope.launch {
            val color = Color.create(red, green, blue)
            colorRepository.applyColor(color)
        }
    }

    public override fun onCleared() {
        viewModelScope.launch {
            colorRepository.disconnect()
        }
        super.onCleared()
    }
}
