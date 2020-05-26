package com.jgeniselli.rgbcontroller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PairedDevicesViewModel(
    private val repository: DevicesRepository
) : ViewModel() {

    private val _progressIsVisible = MutableLiveData<Boolean>()
    private val _devices = MutableLiveData<List<String>>()
    private val _redirectToControl = MutableLiveData<Event<String>>()

    val devices: LiveData<List<String>> get() = _devices
    val progressIsVisible: LiveData<Boolean> get() = _progressIsVisible
    val redirectToControl: LiveData<Event<String>> get() = _redirectToControl

    private var cachedDevices = listOf<PairedDevice>()

    fun onStart() = loadDevices()

    private fun loadDevices() {
        _progressIsVisible.postValue(true)
        viewModelScope.launch {
            cachedDevices = repository.findAllDevices()
            val formattedDevices = cachedDevices.map { it.name }
            _progressIsVisible.postValue(false)
            _devices.postValue(formattedDevices)
        }
    }

    fun onRefresh() = loadDevices()

    fun onDeviceClicked(position: Int) {
        cachedDevices.getOrNull(position)?.let { clickedDevice ->
            _redirectToControl.value = Event(clickedDevice.address)
        } ?: run {
            throw IllegalArgumentException("Invalid device position")
        }
    }
}
