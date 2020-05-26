package com.jgeniselli.rgbcontroller.data.source

interface DevicesRepository {
    suspend fun findAllDevices(): List<PairedDevice>
}

