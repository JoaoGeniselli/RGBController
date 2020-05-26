package com.jgeniselli.rgbcontroller.data.source

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

class BluetoothDevicesRepository(
    private val bluetoothAdapter: BluetoothAdapter
) : DevicesRepository {

    override suspend fun findAllDevices(): List<PairedDevice> {
        return bluetoothAdapter.bondedDevices
            .filter { it.isValid() }
            .map {
                PairedDevice(
                    it.name,
                    it.address
                )
            }
            .toList()
    }

    private fun BluetoothDevice.isValid(): Boolean =
        !name.isNullOrEmpty() && !address.isNullOrEmpty()
}