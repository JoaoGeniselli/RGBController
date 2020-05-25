package com.jgeniselli.rgbcontroller

interface DevicesRepository {
    suspend fun findAllDevices(): List<PairedDevice>
}

data class PairedDevice(val name: String, val address: String) {
    companion object
}