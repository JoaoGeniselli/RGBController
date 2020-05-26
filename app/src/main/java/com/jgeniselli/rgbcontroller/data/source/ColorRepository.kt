package com.jgeniselli.rgbcontroller.data.source

interface ColorRepository {
    suspend fun applyColor(color: Color)
    suspend fun connectToDevice(pairedDeviceAddress: String)
    suspend fun disconnect()
}

