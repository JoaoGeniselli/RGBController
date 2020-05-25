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

    val devices: LiveData<List<String>> get() = _devices
    val progressIsVisible: LiveData<Boolean> get() = _progressIsVisible

    fun onStart() = loadDevices()

    private fun loadDevices() {
        _progressIsVisible.postValue(true)
        viewModelScope.launch {
            val devices = repository.findAllDevices()
            val formattedDevices = devices.map { it.name }
            _progressIsVisible.postValue(false)
            _devices.postValue(formattedDevices)
        }
    }

    fun onRefresh() = loadDevices()
}
