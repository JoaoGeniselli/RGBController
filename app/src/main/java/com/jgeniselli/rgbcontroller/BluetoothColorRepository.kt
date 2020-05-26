package com.jgeniselli.rgbcontroller

import android.bluetooth.BluetoothAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BluetoothColorRepository(private val bluetoothAdapter: BluetoothAdapter) : ColorRepository {

    override suspend fun applyColor(deviceAddress: String, color: Color) {
        GlobalScope.launch(Dispatchers.IO) {
            val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
            device?.let {
                val uuid = device.uuids.firstOrNull()?.uuid
                val socket = device.createRfcommSocketToServiceRecord(uuid)
                socket.connect()
                val outputStream = socket.outputStream
                outputStream?.write(color.toLightDevicePattern().toByteArray())
                socket.close()
            }
        }
    }
}