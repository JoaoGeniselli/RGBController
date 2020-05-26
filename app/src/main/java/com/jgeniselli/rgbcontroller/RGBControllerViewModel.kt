package com.jgeniselli.rgbcontroller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RGBControllerViewModel(
    private val colorRepository: ColorRepository,
    private val pairedDeviceAddress: String
) : ViewModel() {

    fun onApplyClicked(red: Int, green: Int, blue: Int) {
        viewModelScope.launch {
            val color = Color.create(red, green, blue)
            colorRepository.applyColor(pairedDeviceAddress, color)
        }
    }
}
